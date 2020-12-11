#!/usr/bin/env bash

# update PATH
PATH="${__root}/util:$PATH"

LOG_WARN() {
    local content=${1}
    echo -e "\033[31m[WARN] ${content}\033[0m"
}

LOG_INFO() {
    local content=${1}
    echo -e "\033[32m[INFO] ${content}\033[0m"
}


## replace env with value in file
function replace_vars_in_file(){
    file="$1"

    content=$(cat "${file}" | envsubst)
    cat <<< "${content}" > "${file}"
}

## install app
function install(){
    # 系统命令
    command=$1
    # 安装的应用名，如果系统命令不存在，则安装
    app_name=$2

    # install app
    if [[ ! $(command -v "${command}") ]] ;then
        if [[ $(command -v apt) ]]; then
            # Debian/Ubuntu
            LOG_INFO "Start to check and install ${app_name} on remote Debian system ..."
            sudo dpkg -l | grep -qw "${app_name}" || sudo apt install -y "${app_name}"
        elif [[ $(command -v yum) ]]; then
            ## RHEL/CentOS
            LOG_INFO "Start to check and install ${app_name} on remote RHEL system ..."
            sudo rpm -qa | grep -qw "${app_name}" || sudo yum install -y "${app_name}"
        fi
    fi
}

## check and pull docker image
function pull_image(){
    ## set constant
    CDN_BASE_URL="https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/WeBASE/download/docker/image"

    # 镜像名和版本
    repository=$1
    tag=$2
    tar_file_name=$3

    tar_file="${tar_file_name}-${tag}.tar"

    if [[ "$(docker images -q ${repository}:${tag} 2> /dev/null)" == "" ]]; then
        LOG_WARN "Docker image [ ${repository}:${tag} ] not exists!!"
        echo ""
        echo "Pull image [ ${repository}:${tag} ] from ${image_from}!!"
        case ${image_from} in
            cdn )
                wget "${CDN_BASE_URL}/${tar_file}" -O ${tar_file} && docker load -i ${tar_file} && rm -rf ${tar_file}
                ;;
            docker )
                docker pull ${repository}:${tag}
                ;;
            *)
            LOG_WARN "Option '-t' has only two available values: 'cdn' or 'docker'"
            usage
            exit 1;
        esac
    else
        echo "Docker image [ ${repository}:${tag} ] exists."
    fi


    if [[ "$(docker images -q ${repository}:${tag} 2> /dev/null)" == "" ]]; then
        LOG_WARN "Docker image:[${repository}:${tag}] is still missing after pull execution !!"
        echo ""
        LOG_WARN "Please check the network and try to re-run deploy.sh with '-c cdn' parameter."
        exit 5;
    fi
}


## check command
function check_commands(){
    for i in "$@"; do
        if [[ ! $(command -v "$i") ]]; then
            LOG_WARN "[$i] not found, please install [$i]."
            exit 5
        fi
    done
}

## check port is listening
function check_port(){
    port=$1
    service_name=$2

    process_of_port=$(lsof -i -P -n | grep LISTEN | grep -w ":${port}") || :
    if [[ "${process_of_port}x" != "x" ]]; then
        process_name=$(echo ${process_of_port} | awk '{print $1}')
        process_id=$(echo ${process_of_port} | awk '{print $2}')
        LOG_WARN "Port:[${port}] is already in use by a process ${process_id} of [${process_name}], please leave the port:[${port}] for service:[${service_name}]"
        exit 5
    fi
}


function check_docker(){
    echo "=============================================================="

    # start docker
    start_docker

    LOG_INFO "Try to run a hello-world container ..."

    # load hello-world from local tar file
    [[ "$(docker images -q hello-world:latest 2> /dev/null)" == "" ]] && docker load -i ${__root}/hello-world.tar

    if [[ "$(docker run --rm hello-world | grep 'Hello from Docker!')x" != "Hello from Docker!x" ]]; then
        LOG_WARN "Install docker failed !! Please install docker manually with reference:  https://docs.docker.com/engine/install/ "
        exit 5;
    fi
}

## install docker
function install_docker(){
    LOG_INFO "Install Docker ..."

    if [[ ! $(command -v docker) ]]; then
        if [[ $(command -v yum) ]]; then
            ## install containerd.io if on CentOS(RHEL) 8
            # freedesktop.org and systemd
            [[ -f /etc/os-release ]] &&  source /etc/os-release

            container_io_pkg_version="1.3.9-3.1.el7"
            container_io_pkg_name="containerd.io-${container_io_pkg_version}.x86_64.rpm"
            if [[ ${VERSION_ID} -gt 7 ]] && [[ "$(yum list installed | grep -i \"${container_io_pkg_name}\" |grep -i \"${container_io_pkg_version}\")"  == "" ]]; then
                LOG_INFO "On CentOS/RHEL 8.x, install [${container_io_pkg_name}] automatically ..."
                yum -y install "https://download.docker.com/linux/centos/7/x86_64/stable/Packages/${container_io_pkg_name}"
            fi
        fi

        LOG_INFO "Installing Docker ..."
        curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun;
    else
        LOG_INFO "Docker is already installed."
    fi

    check_docker
}

function start_docker(){
    LOG_INFO "Start Docker service ..."
    disable_selinux
    systemctl start docker
}


function disable_selinux(){
    if [[ ! $(command -v setenforce) ]]; then
        LOG_INFO "Disabled SELinux temporarily ..."
        setenforce Permissive
    fi
}

#
function check_directory_exists(){
    parent="$1"
    directory="$2"

    if [[ -d "${2}" ]]; then
        LOG_WARN "Directory:[${parent}/${directory}] exists, BACKUP:[b] or DELETE:[d]?"
        # 调用 readValue
        # 大小写转换
        read_input "BACKUP(b), DELETE(d)? [b/d], 默认: b ? " "^([Bb]|[Dd])$" "b"
        delete_directory=$(echo "${read_value}" | tr [A-Z]  [a-z])

        if [[ "${delete_directory}x" == "dx" ]]; then
            read_input "Confirm to delete directory:[${parent}/${directory}]. (y/n), 默认: n ? " "^([Yy]|[Nn])$" "n"
            confirm_delete_directory=$(echo "${read_value}" | tr [A-Z]  [a-z])
            if [[ "${confirm_delete_directory}x" != "yx" ]]; then
                delete_directory="b"
            fi
        fi

        ## backup or delete
        case ${delete_directory} in
         d)
            LOG_WARN "Delete directory:[${parent}/${directory}] ..."
            rm -rfv ${directory}
            ;;
         b)
            new_dir=${directory}-$(date "+%Y%m%d-%H%M%S")
            LOG_INFO "Backup directory:[${parent}/${directory}] to [${parent}/${new_dir}]..."
            mv -fv ${directory} ${new_dir}
            ;;
         *)
            echo "Unknown operation type : [${OPT_TYPE}]"
            exit 1
        esac
    fi

}


## 获取用户输入
# $1 提示信息
# $2 使用正则校验用户的输入
# $3 默认值
read_value=""
function read_input(){
    read_value=""

    tip_msg="$1"
    regex_of_value="$2"
    default_value="$3"

    until  [[ ${read_value} =~ ${regex_of_value} ]];do
        read -r -p "${tip_msg}" read_value;read_value=${read_value:-${default_value}}
    done
}

function check_memory(){
    echo "=============================================================="
    LOG_INFO "Check minimize available memory ..."
    if [[ $(awk '/^MemAvailable:/ { print $2; }' /proc/meminfo) -lt 1572864 ]]; then
        LOG_WARN "TrustOracle service needs at least 1.5GB memory, please try as follows:
            [1]: Allocate more memory for this server."

        exit 6
   fi
}
check_memory

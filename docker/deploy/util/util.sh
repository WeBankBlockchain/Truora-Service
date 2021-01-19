#!/usr/bin/env bash

#######################################
# 1. Install requirements for deploying Truora:
#   1.1 openssl
#   1.2 curl
#   1.3 wget
#   1.4 Docker and Docker-Compose
# 2. Start Docker
# 3. check requirements for deploying
#######################################

#######################################
# 输出警告(红色)信息
# Globals:
#
# Arguments:
#   $1. info to print
#######################################
LOG_WARN() {
    local content=${1}
    echo -e "\033[31m[WARN] ${content}\033[0m"
}

#######################################
# 输出提示（绿色）信息
# Globals:
#
# Arguments:
#   $1. info to print
#######################################
LOG_INFO() {
    local content=${1}
    echo -e "\033[32m[INFO] ${content}\033[0m"
}

#######################################
# 用环境变量的值替换文件中的占位符
# Globals:
#   all env variables
# Arguments:
#   $1. target file
#######################################
function replace_vars_in_file(){
    tpl_file="$1"
    generated_file="$2"

    content=$(cat "${tpl_file}" | envsubst)
    cat <<< "${content}" > "${generated_file}"
}

#######################################
# 获取用户输入
# Globals:
#    read_value
# Arguments:
#   $1 提示信息
#   $2 使用正则校验用户的输入
#   $3 默认值
#######################################
read_value=""
function read_input(){
    read_value=""

    tip_msg="$1"
    regex_of_value="$2"
    default_value="$3"

    echo ""
    until  [[ ${read_value} =~ ${regex_of_value} ]];do
        read -r -p "${tip_msg}" read_value;read_value=${read_value:-${default_value}}
    done
}

#######################################
# 安装依赖服务

# Globals:
#
# Arguments:
#   $1 服务命令，通过检测命令是否存在来判断是否需要安装该服务
#   $2 服务名
#######################################
function install(){
    # 系统命令
    command=$1
    # 安装的应用名，如果系统命令不存在，则安装
    app_name=$2

    # install app
    if [[ ! $(command -v "${command}") ]] ;then
        if [[ $(command -v apt) ]]; then
            # Debian/Ubuntu
            LOG_INFO "Start to check and install ${app_name} on remote Debian system."
            sudo dpkg -l | grep -qw "${app_name}" || sudo apt install -y "${app_name}"
        elif [[ $(command -v yum) ]]; then
            ## RHEL/CentOS
            LOG_INFO "Start to check and install ${app_name} on remote RHEL system."
            sudo rpm -qa | grep -qw "${app_name}" || sudo yum install -y "${app_name}"
        fi
    else
        LOG_INFO "[${app_name}] already installed."
    fi
}


#######################################
# 在 CentOS 中禁用 SELinux
#
# Globals:
#
# Arguments:
#
#######################################
## disable SELinux on CentOS
function disable_selinux(){
    if [[ $(command -v setenforce) ]]; then
        LOG_INFO "Disabled SELinux temporarily."
        setenforce Permissive || :
    fi
}

#######################################
# 启动 Docker 服务
#
# Globals:
#
# Arguments:
#
#######################################
## start docker
function start_docker(){
    LOG_INFO "Try to start Docker service."
    disable_selinux
    systemctl start docker
}

#######################################
# 检查 Docker 是否安装成功
#
# Globals:
#
# Arguments:
#
#######################################
function check_docker(){
    # start docker
    start_docker

    LOG_INFO "Check Docker is ready to run containers."

    # load hello-world from local tar file
    [[ "$(docker images -q hello-world:latest 2> /dev/null)" == "" ]] && docker load -i ${__root}/hello-world.tar

    if [[ "$(docker run --rm hello-world | grep 'Hello from Docker!')x" != "Hello from Docker!x" ]]; then
        LOG_WARN "Install docker failed !! Please install docker manually with reference:  https://docs.docker.com/engine/install/ "
        exit 5;
    fi
}

#######################################
# 安装 Docker 服务
#
# Globals:
#
# Arguments:
#
#######################################
function install_docker(){
    LOG_INFO "Install Docker."

    if [[ ! $(command -v docker) ]]; then
        if [[ $(command -v yum) ]]; then
            ## install containerd.io if on CentOS(RHEL) 8
            # freedesktop.org and systemd
            [[ -f /etc/os-release ]] &&  source /etc/os-release

            container_io_pkg_version="1.3.9-3.1.el7"
            container_io_pkg_name="containerd.io-${container_io_pkg_version}.x86_64.rpm"
            if [[ ${VERSION_ID} -gt 7 ]] && [[ "$(yum list installed | grep -i \"${container_io_pkg_name}\" |grep -i \"${container_io_pkg_version}\")"  == "" ]]; then
                LOG_INFO "On CentOS/RHEL 8.x, install [${container_io_pkg_name}] automatically."
                yum -y install "https://download.docker.com/linux/centos/7/x86_64/stable/Packages/${container_io_pkg_name}"
            fi
        fi

        LOG_INFO "Installing Docker."
        curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun;
    else
        LOG_INFO "Docker is already installed."
    fi
}


#######################################
# 检测端口是否被占用
#
# Globals:
#
# Arguments:
#   $1 端口号
#   $2 服务
#
#######################################
function check_port(){
    port=$1
    service_name=$2

    process_of_port=""

    if [[ $(command -v sudo) ]]; then
        process_of_port=$(sudo lsof -P -n -w -i ":${port}") || :
    else
        process_of_port=$(lsof -P -n -w -i ":${port}") || :
    fi

    if [[ "${process_of_port}x" != "x" ]]; then
        LOG_WARN "Port:[${port}] is already in use, please leave the port:[${port}] for service:[${service_name}]"
        exit 5
    fi
}

#######################################
# 检查服务是否已经安装
#
# Globals:
#
# Arguments:
#   $1 第一个服务
#   .
#   $n 第 n 个服务
#
#######################################
function check_commands(){
    for i in "$@"; do
        if [[ ! $(command -v "$i") ]]; then
            LOG_WARN "[$i] not found, please install [$i]."
            exit 5
        fi
    done
}

#######################################
# 检查是否存在目录（是否已经部署过服务）
#
# Globals:
#
# Arguments:
#   $1 父目录
#   $2 子目录
#
#######################################
function check_directory_exists(){
    parent="$1"
    directory="$2"

    if [[ -d "${parent}/${directory}" ]]; then
        LOG_WARN "Directory:[${parent}/${directory}] exists, BACKUP:[b] or DELETE:[d]?"
        # 调用 readValue
        # 大小写转换
        read_input "BACKUP(b), DELETE(d)? [b/d], 默认: b ? " "^([Bb]|[Dd])$" "b"
        delete_directory=$(echo "${read_value}" | tr "[:upper:]" "[:lower:]")

        if [[ "${delete_directory}x" == "dx" ]]; then
            read_input "Confirm to delete directory:[${parent}/${directory}]. (y/n), 默认: n ? " "^([Yy]|[Nn])$" "n"
            confirm_delete_directory=$(echo "${read_value}" | tr "[:upper:]" "[:lower:]")
            if [[ "${confirm_delete_directory}x" != "yx" ]]; then
                delete_directory="b"
            fi
        fi

        ## backup or delete
        cd ${parent}
        case ${delete_directory} in
         d)
            LOG_WARN "Delete directory:[${parent}/${directory}]."
            rm -rfv ${directory}
            ;;
         b)
            new_dir=${directory}-$(date "+%Y%m%d-%H%M%S")
            LOG_INFO "Backup directory:[${parent}/${directory}] to [${parent}/${new_dir}]."
            mv -fv ${directory} ${new_dir}
            ;;
         *)
            LOG_WARN "Unknown operation type : [${OPT_TYPE}]"
            exit 1
        esac
    fi

}


#######################################
# 拉取镜像
#
# Globals:
#
# Arguments:
#   $1 cdn 的子目录
#   $2 仓库名
#   $3 镜像版本
#   $4 tar 文件名
#
#######################################
CDN_BASE_URL="https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/WeBankBlockchain/Truora/docker"
function pull_image(){
    # 镜像名和版本
    sub_dir=$1
    repository=$2
    tag=$3
    tar_file_name=$4


    tar_file="${tar_file_name}-${tag}.tar"

    if [[ "$(docker images -q ${repository}:${tag} 2> /dev/null)" == "" ]]; then
        LOG_INFO "Docker image [ ${repository}:${tag} ] not exists!!"
        echo ""
        echo "Pull image [ ${repository}:${tag} ] from ${image_from}!!"
        case ${image_from} in
            cdn )
                wget "${CDN_BASE_URL}/${sub_dir}/${tar_file}" -O ${tar_file} && docker load -i ${tar_file} && rm -rf ${tar_file}
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
        LOG_WARN "Please check the network and try to re-run deploy script."
        exit 5;
    fi
}

#######################################
# 检查可用内
#
# Globals:
#
# Arguments:
#   $1 需要的内存数量
#
#######################################
function check_memory(){
    expected_mem_size=$1
    LOG_INFO "Check minimize available memory."
    if [[ $(awk '/^MemAvailable:/ { print $2; }' /proc/meminfo) -lt ${expected_mem_size:-"1572864"} ]]; then
        LOG_WARN "Truora service needs at least 1.5GB memory, please try as follows:
            [1]: Allocate more memory for this server."
        exit 6
   fi
}


#######################################
# 检查是否存在目录（是否已经部署过服务）
#
# Globals:
#
# Arguments:
#   $1 file
#   $2 错误消息
#
#######################################
function check_file_exists(){
    parent="$1"
    filename="$2"

    if [[ ! -f "${parent}/${filename}" ]] ; then
        LOG_WARN "[ ${filename} ] file not exists in [ ${parent} ]."
        # return false
        return 1
    fi;
    # return true
    return 0
}

#######################################
# 读取 SDK 证书目录
#
# Globals:
#
# Arguments:
#
#
#######################################
function read_sdk_certificate_root(){
    echo ""
    if [[ "${guomi}x" == "nox" ]]; then
        LOG_INFO "Enter sdk path:"
    else
        LOG_INFO "Enter gm sdk path:"
    fi
    while :
    do
        if [[ "${guomi}x" == "nox" ]]; then
            ## ECDSA
            tips="e.g:[ /root/webank/deploy/deploy/fiscobcos/nodes/127.0.0.1/sdk ]: "
            read_input "${tips}" "^/.+$" "."
            sdk_certificate_root=$(realpath -s "${read_value}")

            check_file_exists "${sdk_certificate_root}" "ca.crt" || continue
            check_file_exists "${sdk_certificate_root}" "node.crt" || continue
            check_file_exists "${sdk_certificate_root}" "node.key" || continue
        else
            ## SM2
            tips="e.g:[ /root/webank/deploy/deploy/fiscobcos/nodes/127.0.0.1/sdk/gm ]: "
            read_input "${tips}" "^/.+gm/?$" "."
            sdk_certificate_root=$(realpath -s "${read_value}")

            check_file_exists "${sdk_certificate_root}" "gmca.crt" || continue
            check_file_exists "${sdk_certificate_root}" "gmensdk.crt" || continue
            check_file_exists "${sdk_certificate_root}" "gmensdk.key" || continue
            check_file_exists "${sdk_certificate_root}" "gmsdk.crt" || continue
            check_file_exists "${sdk_certificate_root}" "gmsdk.key" || continue
        fi

        break;
    done
}


################### set bash configurations ###################
# 命令返回非 0 时，就退出
set -o errexit
# 管道命令中任何一个失败，就退出
set -o pipefail
# 遇到不存在的变量就会报错，并停止执行
set -o nounset
# 在执行每一个命令之前把经过变量展开之后的命令打印出来，调试时很有用
#set -o xtrace

# 退出时，执行的命令，做一些收尾工作
trap 'echo -e "Aborted, error $? in command: $BASH_COMMAND"; trap ERR; exit 1' ERR

# Set magic variables for current file & dir
# deploy.sh 脚本所在的目录
__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# 脚本的全路径，包含脚本文件名
__file="${__dir}/$(basename "${BASH_SOURCE[0]}")"
# 脚本的名称，不包含扩展名
__base="$(basename ${__file} .sh)"
# 脚本所在的目录的父目录，一般脚本都会在父项目中的子目录，
#     比如: bin, script 等，需要根据场景修改
#__root="$(cd "$(dirname "${__dir}")" && pwd)" # <-- change this as it depends on your app
__root="${__dir}" # <-- change this as it depends on your app
__root=$(realpath -s "${__root}")

export deploy_root=$(realpath -s "${__root}/..")

################### add current directory to env PATH ###################
PATH="${__root}:$PATH"


echo "============================================================================================"
for arg in "$@"; do

  case ${arg} in

  ins*)
    ## 安装依赖
    LOG_INFO "Install requirements"

    [[ "${deploy_fisco_bcos}x" == "yesx" ]] && install openssl openssl

    install wget wget
    install curl curl

    install_docker
    ;;

  check_requirement*)
    ## 检查依赖
    LOG_INFO "Check requirements."

    chmod +x "${__root}/docker-compose-container"

    [[ "${deploy_fisco_bcos}x" == "yesx" ]] && check_commands openssl

    check_commands curl wget docker "${__root}/docker-compose-container"

    # check docker is running
    check_docker

    # check memory
    export minimal_memory_size=600000
    [[ "${deploy_mysql}x" == "yesx" ]] && total=$((minimal_memory_size + 500000))
    [[ "${deploy_fisco_bcos}x" == "yesx" ]] && total=$((minimal_memory_size + 100000))
    [[ "${deploy_webase_front}x" == "yesx" ]] && total=$((minimal_memory_size + 300000))

    ## TODO, calculate dynamically
    check_memory ${minimal_memory_size}
    ;;

  check_port*)
    ## 检查端口
    LOG_INFO "Check ports."

    # MySQL
    [[ "${deploy_mysql}x" == "yesx" ]] && check_port ${mysql_port} MySQL

    # WeBASE-Front
    [[ "${deploy_webase_front}x" == "yesx" ]] && check_port ${webase_front_port} "WeBASE-Front"

    # FISCO-BCOS
    if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
        # check p2p port
        check_port 30300 "node0 p2p"
        check_port 30301 "node1 p2p"
        check_port 30302 "node2 p2p"
        check_port 30303 "node3 p2p"

        # check channel port
        check_port 20200 "node0 channel"
        check_port 20201 "node1 channel"
        check_port 20202 "node2 channel"
        check_port 20203 "node3 channel"

        # check JSON-RPC port
        check_port 8545 "node0 JSON-RPC"
        check_port 8546 "node1 JSON-RPC"
        check_port 8547 "node2 JSON-RPC"
        check_port 8548 "node3 JSON-RPC"
    fi


    # Truora-Web
    check_port ${truora_web_port} "Truora-Web"

    # Truora-Service
    check_port ${truora_service_port} "Truora-Service"

    ;;

  pull*)
    ## 检查端口
    LOG_INFO "Pull Docker images."

    pull_image "official" "docker/compose" "1.27.4" "docker-compose"

    if [[ "${deploy_webase_front}x" == "yesx" ]]; then
        pull_image "WeBASE" ${webase_front_repository} ${webase_front_version} "webase-front"
    fi

    if [[ "${deploy_mysql}x" == "yesx" ]]; then
        pull_image "official" "mysql" "${mysql_version}" "mysql"
    fi

    if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
        pull_image "FISCO-BCOS" ${fiscobcos_repository} ${fiscobcos_version} "fiscobcos"
    fi


    if [[ "${truora_version}x" == "devx" ]] && [[ "${pull_dev_images}x" == "yesx" ]]; then
        # docker pull latest dev when [-t -p]
        LOG_INFO "Pull latest dev images of Truora-Web"
        docker pull ${truora_web_repository}:${truora_version}
        LOG_INFO "Pull latest dev images of Truora-Service "
        docker pull ${truora_service_repository}:${truora_version}
    fi
    pull_image "truora" ${truora_web_repository} ${truora_version} "truora-web"
    pull_image "truora" ${truora_service_repository} ${truora_version} "truora-service"

    ;;

  deploy*)
    ## deploy
    LOG_INFO "Deploy services ... "

    if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
        LOG_INFO "Generate FISCO-BCOS configurations of version: [${fiscobcos_version}]."
        fisco_bcos_root="${deploy_root}/fiscobcos"
        build_chain_shell="build_chain.sh"

        # cd to fiscobcos dir
        cd "${fisco_bcos_root}"

        ################### check nodes exists ###################
        check_directory_exists "${fisco_bcos_root}" "nodes"

        ################### download build_chain.sh ###################
        # TODO. check MD5 of build_chain.sh
        if [[ ! -f "${build_chain_shell}" ]]; then
            LOG_INFO "Downloading build_chain.sh."
            curl -#L "https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/FISCO-BCOS/releases/${fiscobcos_version}/build_chain.sh" > "${build_chain_shell}" && chmod u+x "${build_chain_shell}"
        fi

        guomi_opt=""
        if [[ "${guomi}x" == "yesx" ]]; then
            guomi_opt=" -g -G "
            sdk_certificate_root="${deploy_root}/fiscobcos/nodes/127.0.0.1/sdk/gm"
        else
            sdk_certificate_root="${deploy_root}/fiscobcos/nodes/127.0.0.1/sdk"
        fi

        ################### generate nodes exists ###################
        LOG_INFO "Generate FISCO-BCOS nodes' config."
        bash ${build_chain_shell} -l "127.0.0.1:4" -d "${guomi_opt}" -v "${fiscobcos_version}"

        LOG_INFO "Replace fiscobcos/docker-compose.yml."
        replace_vars_in_file "${deploy_root}/fiscobcos/node.yml.tpl" "${deploy_root}/fiscobcos/node.yml"

        deploy_output="FISCO-BCOS\t: [ ${fiscobcos_version} ]"
    else
        LOG_INFO "Enter certifications info."
        read_sdk_certificate_root
    fi

    if [[ "${deploy_webase_front}x" == "yesx" ]]; then
        LOG_INFO "Deploy WeBASE-Front of version: [${webase_front_version}]."
        replace_vars_in_file "${deploy_root}/webase/docker-compose.yml.tpl" "${deploy_root}/webase/docker-compose.yml"
        # replace truora-xxx.yml
        if [[ "${guomi}x" == "yesx" ]]; then
            replace_vars_in_file "${deploy_root}/webase/docker-compose-sm2.yml.tpl" "${deploy_root}/webase/docker-compose-sm2.yml"
        else
            replace_vars_in_file "${deploy_root}/webase/docker-compose-ecdsa.yml.tpl" "${deploy_root}/webase/docker-compose-ecdsa.yml"
        fi

        deploy_output="${deploy_output}\nWeBASE-Front\t: [ ${webase_front_version} ]"
    fi

    if [[ "${deploy_mysql}x" == "yesx" ]]; then
        check_directory_exists "${deploy_root}/mysql" "mysql"

        LOG_INFO "Deploy MySQL of version: [${mysql_version}]."
        replace_vars_in_file "${deploy_root}/mysql/docker-compose.yml.tpl" "${deploy_root}/mysql/docker-compose.yml"

        deploy_output="${deploy_output}\nMySQL\t\t: [ ${mysql_version} ]"
    else
        # use the external MySQL service
        LOG_INFO "User external MySQL."

        read_input "Enter MySQL IP, default: 127.0.0.1 ? " "^[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}$" "127.0.0.1"
        mysql_ip=$(echo "${read_value}" | tr "[:upper:]" "[:lower:]")

        read_input "Enter MySQL port, default: 3306 ? " "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" "3306"
        mysql_port="${read_value}"

        read_input "Enter MySQL user, default: truora ? " "^[A-Za-z0-9_]+" "truora"
        mysql_user="${read_value}"

        read_input "Enter MySQL password, default: defaultPassword ? " "^.+$" "defaultPassword"
        mysql_password="${read_value}"

        ## TODO. Check MySQL available
    fi

    LOG_INFO "Deploy Truora of version: [ ${truora_version} ]."

    # mkdir deploy directory
    [[ ! -d "${deploy_root}/truora/deploy" ]] && mkdir "${deploy_root}/truora/deploy" ;

    replace_vars_in_file "${deploy_root}/truora/truora.yml" "${deploy_root}/truora/deploy/truora.yml"
    replace_vars_in_file "${deploy_root}/truora/docker-compose.yml.tpl" "${deploy_root}/truora/deploy/docker-compose.yml"
    replace_vars_in_file "${deploy_root}/truora/truora-web.conf.tpl" "${deploy_root}/truora/deploy/truora-web.conf"

    # replace truora-xxx.yml
    if [[ "${guomi}x" == "yesx" ]]; then
        replace_vars_in_file "${deploy_root}/truora/docker-compose-sm2.yml.tpl" "${deploy_root}/truora/deploy/docker-compose-sm2.yml"
        replace_vars_in_file "${deploy_root}/truora/truora-sm2.yml.tpl" "${deploy_root}/truora/deploy/truora-sm2.yml"
    else
        replace_vars_in_file "${deploy_root}/truora/docker-compose-ecdsa.yml.tpl" "${deploy_root}/truora/deploy/docker-compose-ecdsa.yml"
        replace_vars_in_file "${deploy_root}/truora/truora-ecdsa.yml.tpl" "${deploy_root}/truora/deploy/truora-ecdsa.yml"
    fi

    deploy_output="${deploy_output}\nTruora\t: [ ${truora_version} ]"

    echo "=============================================================="
    LOG_INFO "Generate deploy files success: "
    echo -e "${deploy_output}"
    ;;

  shell*)
    ## 生成启动和停止脚本
    LOG_INFO "Generate START and STOP shell scripts."

    replace_vars_in_file "${__root}/../bin/start.sh.tpl" "${__root}/../start.sh"
    replace_vars_in_file "${__root}/../bin/stop.sh.tpl" "${__root}/../stop.sh"

    ;;
  esac
done



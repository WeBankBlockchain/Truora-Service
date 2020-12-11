#!/usr/bin/env bash

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

# source shell util file
export __root
source ${__root}/util/shell-util.sh

echo "=============================================================="
LOG_INFO "Root dir: [${__root}]"

####### 参数解析 #######
cmdname=$(basename $0)
image_organization=fiscoorg
fiscobcos_version="v2.6.0"
webase_front_version="v1.4.2"
trustoracle_version="v0.5"
guomi="no"
install_deps="no"
# 拉取镜像的方式，cdn、Docker Hub，默认：cdn
image_from="cdn"


# usage help doc.
usage() {
    cat << USAGE  >&2
Usage:
    $cmdname [-g] [-t cdn|docker] [-d] [-w v1.4.2] [-f v2.6.0] [-o v0.5] [-i fiscoorg] [-h]
    -g        Use guomi, default no.
    -t        Where to get docker images, cdn or Docker hub, default cdn.
    -d        Install dependencies during deployment, default no.

    -w        WeBASE-Front version, default v1.4.2
    -f        FISCO-BCOS version, default v2.6.0.
    -o        TrustOracle version, default v0.5.
    -i        Organization of docker images, default fiscoorg.
    -h        Show help info.
USAGE
    exit 1
}

while getopts gt:dw:f:o:i:h OPT;do
    case $OPT in
        g)
            guomi="yes"
            ;;
        t)
            case $OPTARG in
                cdn | docker )
                    ;;
                *)
                LOG_WARN "Invalid value of '-t' parameter, valid are [cdn] or [docker]!"
                    usage
                    exit 1;
            esac
            image_from=$OPTARG
            ;;
        d)
            install_deps="yes"
            ;;
        w)
            webase_front_version=$OPTARG
            ;;
        f)
            fiscobcos_version=$OPTARG
            ;;
        o)
            trustoracle_version=$OPTARG
            ;;
        i)
            image_organization=$OPTARG
            ;;
        h)
            usage
            exit 3
            ;;
        \?)
            usage
            exit 4
            ;;
    esac
done

################### install deps if with -d option ###################
if [[ "${install_deps}x" == "yesx" ]]; then
    echo "=============================================================="
    ## install dependency software
    LOG_INFO "Install requirements ..."

    install openssl openssl
    install wget    wget
    install curl    curl

    install_docker
fi

echo "=============================================================="
# check if deps are installed
LOG_INFO "Check requirements ..."
chmod +x "${__root}/util/docker-compose-container"
check_commands curl wget openssl docker "${__root}/util/docker-compose-container"

# check docker is running
check_docker

echo "=============================================================="
################### check if ports available ###################
LOG_INFO "Check ports are available ..."
# check MySQL
check_port 3306 MySQL

# check WeBASE-Front
check_port 5002 "WeBASE-Front"

# check TrustOracle-Web
check_port 5000 "TrustOracle-Web"

# check TrustOracle-Service
check_port 5012 "TrustOracle-Service"

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


echo "=============================================================="
################### fetch latest build_chain.sh ###################
fisco_bcos_root="${__root}/fiscobcos"
build_chain_shell="build_chain.sh"

# cd to fiscobcos dir
cd "${fisco_bcos_root}"

# download build_chain.sh
# TODO. check MD5 of build_chain.sh
if [[ ! -f "${build_chain_shell}" ]]; then
    LOG_INFO "Downloading build_chain.sh ..."
    curl -#L https://gitee.com/FISCO-BCOS/FISCO-BCOS/raw/master/tools/build_chain.sh > "${build_chain_shell}" && chmod u+x "${build_chain_shell}"
fi

################### generate nodes config ###################
check_directory_exists "${fisco_bcos_root}" "nodes"

guomi_opt=""
if [[ "${guomi}x" == "yesx" ]]; then
    guomi_opt=" -g "
fi

LOG_INFO "Generate FISCO-BCOS nodes' config ..."
bash ${build_chain_shell} -l "127.0.0.1:4" -d "${guomi_opt}"


echo "=============================================================="
################### check images ###################
LOG_INFO "Check docker images exist ..."
echo ""
mysql_repository="mysql"
fiscobcos_repository="fiscoorg/fiscobcos"
trustoracle_service_repository="${image_organization}/trustoracle-service"
trustoracle_web_repository="${image_organization}/trustoracle-web"
webase_front_repository="${image_organization}/webase-front"
mysql_version=5.7

pull_image "docker/compose" "1.27.4" "docker-compose"
pull_image ${mysql_repository} ${mysql_version} "mysql"
pull_image ${fiscobcos_repository} ${fiscobcos_version} "fiscobcos"
pull_image ${trustoracle_web_repository} ${trustoracle_version} "trustoracle-service"
pull_image ${trustoracle_service_repository} ${trustoracle_version} "trustoracle-web"
pull_image ${webase_front_repository} ${webase_front_version} "webase-front"


# guomi option
encrypt_type="0"
if [[ "${guomi}x" == "yesx" ]]; then
    encrypt_type="1"
fi

export image_organization

echo "=============================================================="
################### update webase files ###################
LOG_INFO "Replace config files ..."

echo "Replace webase/webase-front.yml ..."
sed -i "s/encryptType.*#/encryptType: ${encrypt_type} #/g" ${__root}/webase/webase-front.yml

echo "Replace webase/docker-compose.yml ..."
export webase_front_version
replace_vars_in_file ${__root}/webase/docker-compose.yml


################### update fiscobcos files ###################
echo "Replace fiscobcos/docker-compose.yml ..."
export fiscobcos_version
replace_vars_in_file ${__root}/fiscobcos/node.yml

################### update TrustOracle files ###################
echo "Replace trustoracle/trustoracle.yml ..."

cd ${__root}/trustoracle/
check_directory_exists "${__root}/trustoracle/" "mysql"

export trustoracle_version
export mysql_version
replace_vars_in_file ${__root}/trustoracle/docker-compose.yml
sed -i "s/encryptType.*#/encryptType: ${encrypt_type} #/g" ${__root}/trustoracle/trustoracle.yml

echo "=============================================================="
echo ""
LOG_INFO "Deploy TrustOracle service SUCCESS!! Try [ bash start.sh ] and Enjoy!!"
echo ""
LOG_INFO "  Start:[ bash start.sh ]"
LOG_INFO "  Stop :[ bash stop.sh  ]"


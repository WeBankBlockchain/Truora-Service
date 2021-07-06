#!/usr/bin/env bash

################### constant functions ###################
## red warn log
LOG_WARN() {
    local content=${1}
    echo -e "\033[31m[WARN] ${content}\033[0m"
}

## green info log
LOG_INFO() {
    local content=${1}
    echo -e "\033[32m[INFO] ${content}\033[0m"
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

################### set bash configurations ###################
echo "============================================================================================"
LOG_INFO "Current deploy root dir : [ ${__root} ]"

####### 参数解析 #######
cmdname=$(basename $0)

## service config
export deploy_mysql="no"
export deploy_fisco_bcos="no"
export deploy_webase_front="no"

# install deps
export install_deps="no"

# source of images: cdn、Docker Hub，默认：cdn
export image_from="cdn"

## organization of images
export image_organization=fiscoorg

## version
export fiscobcos_version="v2.7.2"
export webase_front_version="v1.5.1"
export truora_version="v1.1.0"
export mysql_version=5.7
export pull_dev_images="no"

## Truora configurations
export truora_web_port=5020
export truora_service_port=5021
export encryption_type="ecdsa"
export truora_profile_list="docker,ecdsa"
export log_level="INFO"

## WeBASE-Front configurations
export webase_front_port=5002

## MySQL configurations
export mysql_ip=127.0.0.1
export mysql_port=3306
export mysql_user=truora
export mysql_password=defaultPassword
export mysql_database=truora

## guomi config
export guomi="no"
export encrypt_type="0"

## sdk certificate path
export fisco_bcos_ip="127.0.0.1"
export fisco_bcos_port="20200"
export fisco_bcos_group="1"
export sdk_certificate_root=""

## output
export deploy_output=""

# usage help doc.
usage() {
    cat << USAGE  >&2
Usage:
    ${cmdname} [-k] [-m] [-w] [-f] [-M 3306] [-W 5002] [-B 5020] [-S 5021] [-d] [-g] [-i fiscoorg] [-t] [-p] [-D] [-h]
    -k        Pull images from Docker hub.

    -m        Deploy a MySQL instance with Docker.
    -w        Deploy a WeBASE-Front service.
    -f        Deploy a 4 nodes FISCO-BCOS service.

    -M        Listen port of MySQL, default 3306.
    -W        Listen port of WeBASE-Front, default 5002.
    -B        Listen port of Truora-Web, default 5020.
    -S        Listen port of Truora-Service, default 5021.

    -d        Install dependencies during deployment.
    -g        Use guomi.

    -i        Organization of docker images, default fiscoorg.
    -t        Use [dev] tag for images of Truora-Service and Truora-Web. Only for test.
    -p        Pull [dev] latest for images of Truora-Service and Truora-Web. Only works when option [-t] is on.
    -D        Set log level of Truora to [ DEBUG ], default [ INFO ].

    -h        Show help info.
USAGE
    exit 1
}

while getopts mwfkM:W:B:S:dgi:tpDh OPT;do
    case ${OPT} in
        m)
            deploy_mysql="yes"
            ;;
        w)
            deploy_webase_front="yes"
            ;;
        f)
            deploy_fisco_bcos="yes"
            ;;
        k)
            image_from="docker"
            ;;
        M)
            if [[ ${OPTARG} =~ "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" ]]; then
                usage
                exit 1;
            fi
            mysql_port=$OPTARG
            ;;
        W)
            if [[ ${OPTARG} =~ "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" ]]; then
                usage
                exit 1;
            fi
            webase_front_port=$OPTARG
            ;;
        B)
            if [[ ${OPTARG} =~ "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" ]]; then
                usage
                exit 1;
            fi
            truora_web_port=$OPTARG
            ;;
        S)
            if [[ ${OPTARG} =~ "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" ]]; then
                usage
                exit 1;
            fi
            truora_service_port=$OPTARG
            ;;
        d)
            install_deps="yes"
            ;;
        g)
            guomi="yes"
            encryption_type="sm2"
            truora_profile_list="docker,sm2"
            encrypt_type="1"
            ;;
        i)
            image_organization=$OPTARG
            ;;
        t)
            truora_version="dev"
            ;;
        p)
            pull_dev_images="yes"
            ;;
        D)
            log_level="DEBUG"
            ;;
        h)
            usage
            exit 1
            ;;
        \?)
            usage
            exit 1
            ;;
    esac
done

################### install deps if with -d option ###################
chmod +x "${__root}/util.sh"
if [[ "${install_deps}x" == "yesx" ]]; then
     bash "${__root}/util.sh" install
fi

# check requirements
bash "${__root}/util.sh" check_requirements

# check ports
bash "${__root}/util.sh" check_ports

# deploy
bash "${__root}/util.sh" deploy

# check and pull images
export fiscobcos_repository="fiscoorg/fiscobcos"
export truora_service_repository="${image_organization}/truora-service"
export truora_web_repository="${image_organization}/truora-web"
export webase_front_repository="${image_organization}/webase-front"

bash "${__root}/util.sh" pull

# generate start shell
bash "${__root}/util.sh" shell

echo ""
LOG_INFO "Deploy Truora service SUCCESS!! Try [ bash start.sh ] and Enjoy!!"
echo ""
LOG_INFO "  Start:[ bash start.sh ]"
LOG_INFO "  Stop :[ bash stop.sh  ]"



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
export fiscobcos_version="v2.6.0"
export webase_front_version="v1.4.2"
export trustoracle_version="v1.0.0"
export mysql_version=5.7

## TrustOracle configurations
export trustoracle_web_port=5000
export trustoracle_service_port=5012

## WeBASE-Front configurations
export webase_front_port=5002

## MySQL configurations
export mysql_ip=127.0.0.1
export mysql_port=3306
export mysql_user=trustoracle
export mysql_password=defaultPassword
export mysql_database=trustoracle

## guomi config
export guomi="no"

## sdk certificate path
export fisco_bcos_ip="127.0.0.1"
export fisco_bcos_port="20200"
export fisco_bcos_group="1"
export sdk_certificate_root="../fiscobcos/nodes/127.0.0.1/sdk"

# usage help doc.
usage() {
    cat << USAGE  >&2
Usage:
    $cmdname [-k] [-m] [-w] [f] [-M 3306] [-W 5002] [-B 5000] [-S 5012] [-d] [-g] [-i fiscoorg] [-h]
    -k        Pull images from Docker hub.

    -m        Deploy a MySQL instance with Docker, default no, use an external MySQL service.
    -w        Deploy a WeBASE-Front service, default no.
    -f        Deploy a 4 nodes FISCO-BCOS service, default no.

    -M        Listen port of MySQL, default 3306.
    -W        Listen port of WeBASE-Front, default 5002.
    -B        Listen port of TrustOracle-Web, default 5000.
    -S        Listen port of TrustOracle-Service, default 5012.

    -d        Install dependencies during deployment, default no.
    -g        Use guomi, default no.

    -i        Organization of docker images, default fiscoorg.

    -h        Show help info.
USAGE
    exit 1
}

while getopts kmwfM:W:B:S:dgi:h OPT;do
    case ${OPT} in
        k)
            image_from="docker"
            ;;
        m)
            deploy_mysql="yes"
            ;;
        w)
            deploy_webase_front="yes"
            ;;
        f)
            deploy_fisco_bcos="yes"
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
            trustoracle_web_port=$OPTARG
            ;;
        S)
            if [[ ${OPTARG} =~ "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-5]{2}[0-3][0-5])$" ]]; then
                usage
                exit 1;
            fi
            trustoracle_service_port=$OPTARG
            ;;
        d)
            install_deps="yes"
            ;;
        g)
            guomi="yes"
            ;;
        i)
            image_organization=$OPTARG
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
export trustoracle_service_repository="${image_organization}/trustoracle-service"
export trustoracle_web_repository="${image_organization}/trustoracle-web"
export webase_front_repository="${image_organization}/webase-front"

bash "${__root}/util.sh" pull

# generate start shell
bash "${__root}/util.sh" shell

echo ""
LOG_INFO "Deploy TrustOracle service SUCCESS!! Try [ bash start.sh ] and Enjoy!!"
echo ""
LOG_INFO "  Start:[ bash start.sh ]"
LOG_INFO "  Stop :[ bash stop.sh  ]"


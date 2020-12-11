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
# 脚本所在的目录
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


# 启动中间件服务
LOG_INFO "String FISCO-BCOS nodes...."
cd "${__dir}/fiscobcos" && docker-compose-container up -d

LOG_INFO "String WeBASE-Front...."
cd "${__dir}/webase" && docker-compose-container up -d

LOG_INFO "String TrustOracle...."
cd "${__dir}/trustoracle" && docker-compose-container up -d


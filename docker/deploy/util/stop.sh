#!/usr/bin/env bash

# 命令返回非 0 时，就退出
#set -o errexit
# 管道命令中任何一个失败，就退出
set -o pipefail
# 遇到不存在的变量就会报错，并停止执行
set -o nounset
# 在执行每一个命令之前把经过变量展开之后的命令打印出来，调试时很有用
#set -o xtrace

# 退出时，执行的命令，做一些收尾工作
trap 'echo -e "Aborted, error $? in command: $BASH_COMMAND"; trap ERR; exit 1' ERR


PATH="${root_dir}/util":$PATH

echo "=============================================================="
echo "Root dir: [${root_dir}]"

# 停止服务
echo "Stop TrustOracle.."

cd "${root_dir}/trustoracle" && docker-compose-container down

if [[ "${deploy_webase_front}x" == "yesx" ]]; then
    echo "Stop WeBASE-Front."
    cd "${root_dir}/webase" && docker-compose-container down
fi

if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
    echo "Stop FISCO-BCOS."
    cd "${root_dir}/fiscobcos" && docker-compose-container down
fi

if [[ "${deploy_mysql}x" == "yesx" ]]; then
    echo "Stop MySQL."
    cd "${root_dir}/mysql" && docker-compose-container down
fi


echo "Stop All SUCCESS !!!"

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

# source shell util file
PATH="${root_dir}/util":$PATH

echo "=============================================================="
echo "Root dir: [${root_dir}]"


if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
    echo "Start FISCO-BCOS."
    cd "${root_dir}/fiscobcos" && docker-compose-container up -d

    bash "${root_dir}/util/wait_tcp.sh" 20200 60
    echo "FISCO-BCOS start SUCCESS !!!"
fi

# 启动中间件服务
if [[ "${deploy_webase_front}x" == "yesx" ]]; then
    echo "Start WeBASE-Front."
    cd "${root_dir}/webase" && docker-compose-container up -d

    bash "${root_dir}/util/wait_tcp.sh" ${webase_front_port} 60
    echo "WeBASE-Front start SUCCESS !!!"
fi

if [[ "${deploy_mysql}x" == "yesx" ]]; then
    echo "Start MySQL."
    cd "${root_dir}/mysql" && docker-compose-container up -d
    bash "${root_dir}/util/wait_tcp.sh" ${mysql_port} 60
    echo "MySQL start SUCCESS !!!"
fi

echo "String TrustOracle.."
cd "${root_dir}/trustoracle" && docker-compose-container up -d

bash "${root_dir}/util/wait_tcp.sh" ${trustoracle_service_port} 60
bash "${root_dir}/util/wait_tcp.sh" ${trustoracle_web_port} 60

echo "TrustOracle start SUCCESS !!!"


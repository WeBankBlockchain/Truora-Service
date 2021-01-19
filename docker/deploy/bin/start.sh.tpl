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
PATH="${deploy_root}/util":$PATH

echo "=============================================================="
echo "Root dir: [${deploy_root}]"


if [[ "${deploy_fisco_bcos}x" == "yesx" ]]; then
    echo "Start FISCO-BCOS."
    cd "${deploy_root}/fiscobcos" && docker-compose-container up -d

    bash "${deploy_root}/util/wait_tcp.sh" -p 20200 \
        -m "Wait for FISCO-BCOS nodes start up..." \
        -s "FISCO-BCOS nodes start success." \
        -f "Check FISCO-BCOS nodes start TIMEOUT!! Try to restart or check in log: [ ${deploy_root}/fiscobcos/nodes/127.0.0.1/node0/log/ ]"
fi

# 启动中间件服务
if [[ "${deploy_webase_front}x" == "yesx" ]]; then
    echo "Start WeBASE-Front."
    cd "${deploy_root}/webase" && docker-compose-container up -d

    bash "${deploy_root}/util/wait_tcp.sh" -p ${webase_front_port} \
        -m "Wait for WeBASE-Front start up on port:[${webase_front_port}]..." \
        -s "WeBASE-Front start success." \
        -f "Check WeBASE-Front start TIMEOUT!! Try to restart or check error in log: [ ${deploy_root}/webase/log/WeBASE-Front.log ]"
fi

if [[ "${deploy_mysql}x" == "yesx" ]]; then
    echo "Start MySQL."
    cd "${deploy_root}/mysql" && docker-compose-container up -d

    # sleep 20s to wait MySQL restart
    sleep 20s

    bash "${deploy_root}/util/wait_tcp.sh" -p ${mysql_port}  \
        -m "Wait for MySQL start up on port:[${mysql_port}]..." \
        -s "MySQL start success." \
        -f "Check MySQL start TIMEOUT!! Try to restart or check error with command: [ docker logs truora-mysql ]"
fi

echo "String Truora.."
cd "${deploy_root}/truora/deploy" && docker-compose-container up -d

bash "${deploy_root}/util/wait_tcp.sh" -p ${truora_service_port} \
        -m "Wait for Truora-Service start up on port:[${truora_service_port}]..." \
        -s "Truora-Service start success." \
        -f "Check Truora-Service start TIMEOUT!! Try to restart or check error in log: [ ${deploy_root}/truora/deploy/log/server/Oracle-Service.log ]"

bash "${deploy_root}/util/wait_tcp.sh" -p ${truora_web_port}  \
        -m "Wait for Truora-Web start up on port:[${truora_web_port}]..." \
        -s "Truora-Web start SUCCESS." \
        -f "Check Truora-Web start TIMEOUT!! Try to restart or check error in log: [ ${deploy_root}/truora/deploy/log/nginx/error.log ]"

echo "Truora service start up SUCCESS !!"

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

    bash "${deploy_root}/util/wait_tcp.sh" -p 20200 -t 60 \
        -m "Wait for FISCO-BCOS nodes start up..." \
        -s "FISCO-BCOS nodes start success." \
        -f "FISCO-BCOS nodes start failed!! Check error with command: [ tail -n 100 $(ls -t ${deploy_root}/fiscobcos/nodes/127.0.0.1/node0/log/ |head -n 1) ]"
fi

# 启动中间件服务
if [[ "${deploy_webase_front}x" == "yesx" ]]; then
    echo "Start WeBASE-Front."
    cd "${deploy_root}/webase" && docker-compose-container up -d

    bash "${deploy_root}/util/wait_tcp.sh" -p ${webase_front_port} -t 60 \
        -m "Wait for WeBASE-Front start up on port:[${webase_front_port}]..." \
        -s "WeBASE-Front start success." \
        -f "WeBASE-Front start failed!! Check error with command: [ tail -n 200 ${deploy_root}/webase/log/WeBASE-Front-error.log ]"
fi

if [[ "${deploy_mysql}x" == "yesx" ]]; then
    echo "Start MySQL."
    cd "${deploy_root}/mysql" && docker-compose-container up -d

    # sleep 20s to wait MySQL restart
    sleep 20s

    bash "${deploy_root}/util/wait_tcp.sh" -p ${mysql_port} -t 60 \
        -m "Wait for MySQL start up on port:[${mysql_port}]..." \
        -s "MySQL start success." \
        -f "MySQL start failed!! Check error with command: [ docker logs trustoracle-mysql ]"
fi

echo "String TrustOracle.."
cd "${deploy_root}/trustoracle/deploy" && docker-compose-container up -d

bash "${deploy_root}/util/wait_tcp.sh" -p ${trustoracle_service_port} -t 60 \
        -m "Wait for TrustOracle-Service start up on port:[${trustoracle_service_port}]..." \
        -s "TrustOracle-Service start success." \
        -f "TrustOracle-Service start failed!! Check error with command: [ tail -n 100 ${deploy_root}/trustoracle/deploy/log/server/Oracle-Service.log ]"

bash "${deploy_root}/util/wait_tcp.sh" -p ${trustoracle_web_port} -t 60 \
        -m "Wait for TrustOracle-Web start up on port:[${trustoracle_web_port}]..." \
        -s "TrustOracle-Web start SUCCESS." \
        -f "TrustOracle-Web start failed!! Check error with command: [ tail -n 10 ${deploy_root}/trustoracle/deploy/log/nginx/error.log ]"

echo "TrustOracle service start up SUCCESS !!"

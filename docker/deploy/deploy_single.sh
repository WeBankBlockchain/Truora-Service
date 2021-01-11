#!/usr/bin/env bash

## green info log
LOG_INFO() {
    local content=${1}
    echo -e "\033[32m[INFO] ${content}\033[0m"
}

echo "============================================================================================"
LOG_INFO "call script [./util/deploy_util.sh] "


set -x
bash ./util/deploy_util.sh $@

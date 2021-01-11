#!/usr/bin/env bash
# 该脚本只能检测 本机指定的 端口是否 可用.

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
# 获取当前时间
# Globals:
#
# Arguments:
#######################################
function now {
	date +%s
}

#######################################
# 检查是否超时
# Globals:
#
# Arguments:
#######################################
function checkTimeout {
	if [[ ${TIMEOUT} -gt 0 ]]; then
		current=$(now)
		elapsed=$(( $current - $startTime ))
		if [[ ${elapsed} -ge ${TIMEOUT} ]]; then
			LOG_WARN "$(echo ${FAILED_MESSAGE} | envsubst)"
			echo ""
			LOG_INFO "Restart command: [ bash stop.sh && bash start.sh ]"
			exit 2
		fi
	fi
}

#######################################
# 检查端口是否监听
# Globals:
#
# Arguments:
#######################################
function checkPort {
	fuser ${PORT}/tcp 2>/dev/null | wc -c
}




# 脚本名
__cmd="$(basename $0)"

export PORT=22
export TIMEOUT=120
export MESSAGE="Waiting for TCP port \${PORT}"
export SUCCESS_MESSAGE="Service on port \${PORT} start success."
export FAILED_MESSAGE="Wait for service on port \${PORT} start failed."

# 解析参数
# usage help doc.
usage() {
    cat << USAGE  >&2
Usage:
    ${__cmd}    [-p port] [-t timeout] [-m message] [-s success_message] [-f failed_message]
    -p          Required. TCP port number: [0-65535], default 22.

    -m          Optional. Tip message, default: 'Waiting for TCP port \${PORT}'.
    -s          Optional. Success tip message, default: 'Service on port \${PORT} start success.'.
    -f          Optional. Failed tip message, default: 'Wait for service on port \${PORT} start failed.'.
    -t          Optional. Timeout is expressed in seconds, default 120s, if timeout <= 0, no timeout.
    -h          Show help info.
USAGE
    exit 1
}

while getopts p:t:m:s:f:h OPT;do
    case ${OPT} in
        p)
            PORT=$OPTARG
            if [[ "${PORT}" -lt 0 ]] || [[ "${PORT}" -gt 65535 ]]; then
                LOG_WARN "Invalid port number: (0,65535)."
                usage
                exit 2
            fi
            ;;
        t)
            if [[ "${OPTARG}" -lt 300 ]]; then
                TIMEOUT=$OPTARG
            else
                LOG_WARN "Max timeout is 300s, set timeout to 300s."
                TIMEOUT=300
            fi
            ;;
        m)
            MESSAGE=$OPTARG
            ;;
        s)
            SUCCESS_MESSAGE=$OPTARG
            ;;
        f)
            FAILED_MESSAGE=$OPTARG
            ;;
        h)
            usage
            exit 2
            ;;
        \?)
            usage
            exit 2
            ;;
    esac
done


SLEEP=1s

startTime=$(now)


LOG_INFO "$(echo ${MESSAGE} | envsubst)"
#if [[ $(checkPort) = 0 ]]; then
#    LOG_INFO "$(echo ${MESSAGE} | envsubst)"
#fi

while [[ $(checkPort) = 0 ]]
do
  checkTimeout
  sleep ${SLEEP}
done

#OWNER=$(fuser ${PORT}/tcp 2>/dev/null | sed -e 's/\s*//g')
#LOG_INFO "$(echo ${SUCCESS_MESSAGE} | envsubst): $OWNER"
LOG_INFO "$(echo ${SUCCESS_MESSAGE} | envsubst)"
echo ""
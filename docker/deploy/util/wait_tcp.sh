#!/usr/bin/env bash
# 该脚本只能检测 本机指定的 端口是否 可用.

function help {
	echo "Usage: bash wait_for.sh port [timeout]"
	echo "       port is a TCP port number, or the service name (for instance http, ssh)"
	echo "       timeout is expressed in seconds"
	echo "             optional (defaulted to 30)"
	echo "             if <= 0, no timeout"
	exit 1
}

if [[ $# -eq 0 ]]; then
	help
fi

PORT=$1

# timeout in seconds
TIMEOUT=30
if [[ $# -ge 2 ]]; then
	TIMEOUT=$2
fi

SLEEP=1s

function now {
	date +%s
}

startTime=$(now)

function checkTimeout {
	if [[ ${TIMEOUT} -gt 0 ]]; then
		current=$(now)
		elapsed=$(( $current - $startTime ))
		if [[ ${elapsed} -ge ${TIMEOUT} ]]; then
			echo "Timeout"
			exit 2
		fi
	fi
}

function checkPort {
	fuser ${PORT}/tcp 2>/dev/null | wc -c
}

if [[ $(checkPort) = 0 ]]; then
	echo "Waiting for TCP port $PORT"
fi

while [[ $(checkPort) = 0 ]]
do
  checkTimeout
  sleep ${SLEEP}
done

OWNER=$(fuser ${PORT}/tcp 2>/dev/null | sed -e 's/\s*//g')
echo "$OWNER"
package com.webank.oracle.event.exception;

import com.webank.oracle.base.enums.ReqStatusEnum;

public class RemoteCallException extends EventBaseException {
    public RemoteCallException(ReqStatusEnum reqStatusEnum, Object ... paramArray){
        super(reqStatusEnum,paramArray);
    }
}

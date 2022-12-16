package com.webank.truora.base.exception;

import com.webank.truora.base.enums.ReqStatusEnum;

public class RemoteCallException extends EventBaseException {
    public RemoteCallException(ReqStatusEnum reqStatusEnum, Object ... paramArray){
        super(reqStatusEnum,paramArray);
    }
}

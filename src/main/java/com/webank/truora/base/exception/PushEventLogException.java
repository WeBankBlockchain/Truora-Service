package com.webank.truora.base.exception;

import com.webank.truora.base.enums.ReqStatusEnum;

public class PushEventLogException extends EventBaseException {
    public PushEventLogException(ReqStatusEnum reqStatusEnum, Object ... paramArray){
        super(reqStatusEnum,paramArray);
    }
}

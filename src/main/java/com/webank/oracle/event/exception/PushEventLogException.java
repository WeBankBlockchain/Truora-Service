package com.webank.oracle.event.exception;

import com.webank.oracle.base.enums.ReqStatusEnum;

public class PushEventLogException extends EventBaseException {
    public PushEventLogException(ReqStatusEnum reqStatusEnum, Object ... paramArray){
        super(reqStatusEnum,paramArray);
    }
}

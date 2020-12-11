package com.webank.oracle.event.exception;

import com.webank.oracle.base.enums.ReqStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FullFillException extends EventBaseException {

    public FullFillException(ReqStatusEnum reqStatusEnum, Object ... paramArray) {
        super(reqStatusEnum, paramArray);
    }
}

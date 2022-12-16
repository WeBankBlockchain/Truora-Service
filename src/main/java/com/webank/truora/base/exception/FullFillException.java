package com.webank.truora.base.exception;

import com.webank.truora.base.enums.ReqStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FullFillException extends EventBaseException {

    public FullFillException(ReqStatusEnum reqStatusEnum, Object ... paramArray) {
        super(reqStatusEnum, paramArray);
    }
}

package com.webank.truora.base.exception;

import com.webank.truora.base.enums.ReqStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class JsonParseException extends EventBaseException {

    public JsonParseException(ReqStatusEnum reqStatusEnum, Object ... paramArray) {
        super(reqStatusEnum, paramArray);
    }
}

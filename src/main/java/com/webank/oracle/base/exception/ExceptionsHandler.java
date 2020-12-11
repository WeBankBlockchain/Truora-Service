/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.oracle.base.exception;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.pojo.vo.RetCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

import static com.webank.oracle.base.utils.JsonUtils.toJSONString;

/**
 * catch an handler exception.
 */
@ControllerAdvice
@Log4j2
public class ExceptionsHandler {

    /**
     * catch：NodeMgrException.
     */
    @ResponseBody
    @ExceptionHandler(value = OracleException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse myExceptionHandler(OracleException nodeMgrException) {
        log.warn("catch business exception", nodeMgrException);
        RetCode RetCode = Optional.ofNullable(nodeMgrException).map(OracleException::getCodeAndMsg)
                .orElse(ConstantCode.SYSTEM_EXCEPTION);

        BaseResponse bre = new BaseResponse(RetCode);
        log.warn("business exception return:{}", toJSONString(bre));
        return bre;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse validExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> fieldError = e.getBindingResult().getAllErrors();

//        String errFieldStr = fieldError.stream()
//                .map(obj -> toJavaObjectList(toJSONString(obj),Object.class))
//                .map(err -> err."field")
//                .collect(Collectors.joining(","));
//        StringUtils.removeEnd(errFieldStr, ",");
//        String message = "these fields can not be match:" + errFieldStr;
        String message = "these fields can not be match:" ;
        RetCode RetCode = new RetCode(ConstantCode.PARAM_EXCEPTION.getCode(), message);
        BaseResponse bre = new BaseResponse(RetCode);
        return bre;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse validExceptionHandler(HttpMessageNotReadableException e) {
        String message = "these fields can not be match:" + e.getMessage();
        RetCode RetCode = new RetCode(ConstantCode.PARAM_EXCEPTION.getCode(), message);
        BaseResponse bre = new BaseResponse(RetCode);
        return bre;
    }


    /**
     * parameter exception:TypeMismatchException
     */
    @ResponseBody
    @ExceptionHandler(value = TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse typeMismatchExceptionHandler(TypeMismatchException ex) {
        log.warn("catch typeMismatchException", ex);

        RetCode RetCode = new RetCode(ConstantCode.PARAM_EXCEPTION.getCode(), ex.getMessage());
        BaseResponse bre = new BaseResponse(RetCode);
        log.warn("typeMismatchException return:{}", toJSONString(bre));
        return bre;
    }


    /**
     * catch：RuntimeException.
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse exceptionHandler(RuntimeException exc) {
        log.warn("catch RuntimeException", exc);
        // 默认系统异常
        RetCode RetCode = ConstantCode.SYSTEM_EXCEPTION;

        BaseResponse bre = new BaseResponse(RetCode);
        log.warn("system RuntimeException return:{}", toJSONString(bre));
        return bre;
    }
}
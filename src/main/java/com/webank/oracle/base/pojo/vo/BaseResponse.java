/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.oracle.base.pojo.vo;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import lombok.Data;

/**
 * Entity class of response info.
 */
@Data
public class BaseResponse {

    private int code;
    private String message;
    private Object data;
    private long totalCount;

    public BaseResponse() {
    }

    public BaseResponse(RetCode retcode) {
        this.code = retcode.getCode();
        this.message = retcode.getMessage();
    }

    public BaseResponse(RetCode retcode, Object data) {
        this.code = retcode.getCode();
        this.message = retcode.getMessage();
        this.data = data;
    }

    public static BaseResponse pageResponse(RetCode retcode, List<?> data, long totalCount) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.code = retcode.getCode();
        baseResponse.message = retcode.getMessage();
        baseResponse.data = CollectionUtils.isEmpty(data) ? Collections.emptyList() : data;
        baseResponse.totalCount = totalCount;
        return baseResponse;
    }
    public static BaseResponse emptyPageResponse(RetCode retcode) {
        return pageResponse(retcode,Collections.emptyList(), 0);
    }
}

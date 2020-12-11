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

/**
 * A-BB-CCC A:error level. <br/>
 * 1:system exception <br/>
 * 2:business exception <br/>
 * B:project number <br/>
 * Oracle-Service:05 <br/>
 * C: error code <br/>
 */
// TODO. refactor
public class ConstantCode {

    /* return success */
    public static final RetCode SUCCESS = RetCode.mark(0, "success");

    /* system exception */
    public static final RetCode SYSTEM_EXCEPTION = RetCode.mark(105000, "system exception");

    /*Business exception */
    public static final RetCode NEW_KEY_STORE_FAIL = RetCode.mark(205000, "create keyStore exception");
    public static final RetCode SEND_TRANSACTION_FAIL = RetCode.mark(205001, "send transaction exception");
    public static final RetCode GROUP_ID_NOT_EXIST = RetCode.mark(205002, "group id not exist");
    public static final RetCode DATA_SIGN_ERROR = RetCode.mark(205003, "data sign error");
    public static final RetCode DEPLOY_FAILED = RetCode.mark(205004, "deploy failed");
    public static final RetCode DATA_NOT_EXISTS = RetCode.mark(205005, "Data not exists");
    public static final RetCode DECODE_PROOF_ERROR = RetCode.mark(205006, "Decode proof error");
    public static final RetCode REGISTER_CONTRACT_NOT_CONFIGURED = RetCode.mark(205007,
            "Register center contract name and version not configured.");
    public static final RetCode FETCH_ORACLE_SERVICE_LIST_ERROR = RetCode.mark(205008,
            "Fetch oracle service list error.");
    public static final RetCode CHAIN_OR_GROUP_ID_PARAM_ERROR = RetCode.mark(205009,
            "Chain id and group id should great than 0.");
    public static final RetCode NO_CNS_SERVICE_ERROR = RetCode.mark(205010,
            "No cns service error.");
    public static final RetCode CHECK_CONTRACT_VALID_ERROR = RetCode.mark(205011,
            "Check contract valid error.");

    /* param exception */
    public static final RetCode PARAM_EXCEPTION = RetCode.mark(405000, "param exception");

}

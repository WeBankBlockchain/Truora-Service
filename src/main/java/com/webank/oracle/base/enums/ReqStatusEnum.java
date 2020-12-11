/**
 *
 */


package com.webank.oracle.base.enums;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReqStatusEnum {
    SUCCESS(0, "success"),

    // base exception
    UNEXPECTED_EXCEPTION_ERROR(205101, "Unknown unexpected exception:[%s]."),
    REQ_ALREADY_EXISTS(205102, "Req already exists error:[%s]."),

    // call go lib error
    VRF_LIB_FILE_NOT_EXISTS(205111, "VRF lib file not exists."),
    VRF_LIB_LOAD_ERROR(205112, "Load VRF lib error."),

    // network error
    UNKNOWN_SOCKET_ERROR(205121, "Unknown socket error:[%s]."),
    HOST_UNAVAILABLE_ERROR(205122, "Remote host is unavailable:[%s]."),
    WRITE_TIMEOUT_ERROR(205123, "Write data to remote host timeout:[%s]."),
    READ_TIMEOUT_ERROR(205124, "Read from remote host timeout:[%s]."),

    // http response code error
    _404_NOT_FOUND_ERROR(205131, "Request url not found:[404]."),
    _500_SERVER_ERROR(205132, "Remote server internal error:[5005]."),
    OTHER_CODE_ERROR(205133, "Http code:[%s] from remote, not 200."),

    // http response format error
    EMPTY_RESPONSE_ERROR(205141, "Empty response from remote."),
    RESULT_FORMAT_ERROR(205142, "Return data format:[%s] error:[%s]."),
    PARSE_RESULT_ERROR(205143, "Parse result:[%s] by format:[%s] error."),

    // http unknown error
    REMOTE_CALL_UNEXPECTED_EXCEPTION_ERROR(205151, "Remote call with unexpected exception:[%s]."),

    // full fill error
    VRF_CONTRACT_ADDRESS_ERROR(205161, "VRF contract address is empty."),
    ORACLE_CORE_CONTRACT_ADDRESS_ERROR(205162, "Oracle core contract address is empty."),
    UPLOAD_RESULT_TO_CHAIN_ERROR(205163, "Upload result to chain failed:[%s]."),

    ;

    private int status;
    private String format;


    /**
     *
     * @param args
     * @return
     */
    public String format(Object ... args){
        try {
            return String.format(this.format, args );
        }catch (Exception e){
            return String.format("%s:%s", this.format, StringUtils.join(args,","));
        }
    }

    /**
     *
     * @param errorMsg
     * @return
     */
    public static ReqStatusEnum getBySocketErrorMsg(String errorMsg){
        if (StringUtils.containsIgnoreCase(errorMsg, "connect")) {
            return HOST_UNAVAILABLE_ERROR;
        } else if (StringUtils.containsIgnoreCase(errorMsg, "read")) {
            return READ_TIMEOUT_ERROR;
        } else if (StringUtils.containsIgnoreCase(errorMsg, "write")) {
            return WRITE_TIMEOUT_ERROR;
        }
        return UNKNOWN_SOCKET_ERROR;
    }
}
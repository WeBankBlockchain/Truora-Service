package com.webank.oracle.base.exception;

import com.webank.oracle.base.pojo.vo.RetCode;

public class OracleException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private RetCode retCode;

    public OracleException(RetCode RetCode) {
        super(RetCode.getMessage());
        this.retCode = RetCode;
    }

    public OracleException(RetCode RetCode, Throwable cause) {
        super(RetCode.getMessage(), cause);
        this.retCode = RetCode;
    }

    public OracleException(int code, String msg) {
        super(msg);
        this.retCode = new RetCode(code, msg);
    }

    public OracleException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.retCode = new RetCode(code, msg);
    }

    public RetCode getCodeAndMsg() {
        return retCode;
    }

}

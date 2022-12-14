package com.webank.truora.dapps;

import com.webank.truora.base.enums.ReturnTypeEnum;
import lombok.Data;

import java.math.BigInteger;

@Data
public class GeneralResult {
    public GeneralResult(){

    }
    public GeneralResult(ReturnTypeEnum returnType_){
        returnType = returnType_;
    }
    ReturnTypeEnum returnType;
    BigInteger intValue;
    byte[] bytesValue;
    String strValue;
    boolean isValid =false;

    public void put(Object val){
        switch (returnType)
        {
            case INT256:
                this.intValue = (BigInteger) val;
                break;
            case STRING:
                this.strValue = (String)val;
                break;
            case BYTES:
                this.bytesValue = (byte[])val;
                break;
            default:
                return ;

        }
        isValid = true;
    }

    public boolean checkValid(){
        return isValid;
    }
}

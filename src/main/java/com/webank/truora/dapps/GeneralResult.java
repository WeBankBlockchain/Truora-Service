package com.webank.truora.dapps;

import com.webank.truora.base.enums.ReturnTypeEnum;
import lombok.Data;

import java.math.BigInteger;


/*一个简单的包装器，容纳数据的枚举，以及预言机合约支持的几个不同类型的值*/
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

    public void put(Object val,ReturnTypeEnum returnType_)
    {
        this.returnType = returnType_;
        switch (this.returnType)
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
    public void put(Object val){
        put(val,this.returnType);
    }

    public boolean checkValid(){
        return isValid;
    }

    public String descriptData(){
        String desc ="";
        desc += "Type : "+this.returnType + " ; ";
        desc += "Valid : "+this.isValid + " ; ";
        switch (this.returnType)
        {
            case INT256:
                desc += "Result : " + this.intValue;
                break;
            case STRING:
                desc += "Result : " + this.strValue ;
                break;
            case BYTES:
                desc += "Result :" + this.bytesValue;
                break;
            default:
                desc += "Result : (missing)";

        }
        return desc;
    }
}

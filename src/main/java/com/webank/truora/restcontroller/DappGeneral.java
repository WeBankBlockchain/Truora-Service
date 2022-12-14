package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.pojo.vo.RetCode;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.dapps.GeneralOracleClient;
import com.webank.truora.dapps.GeneralOracleConfig;
import com.webank.truora.dapps.GeneralOracleSource;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/*演示的restful 入口
*
* 从默认配置的url里获取一个随机数
*    "http://localhost:5022/truora/dapps/get",
* 指定了url序号，对应application-dpps.yaml配置里 GeneralOracle段里的3个url。
* 为了使测试时有较强的确定性，所以，配置为指定名字，不用数组方式
    "http://localhost:5022/truora/dapps/get?url=1",
    "http://localhost:5022/truora/dapps/get?url=2",
    "http://localhost:5022/truora/dapps/get?url=3",
*
* */
@Api(value = "/dapps", tags = "dapps demo")
@Slf4j
@RestController
@RequestMapping(value = "/dapps",produces = {"application/JSON"})
public class DappGeneral {

    @Autowired private Bcos3EventRegisterFactory bcos3EventRegisterFactory;
    @Autowired private GeneralOracleConfig generalOracleConfig;
    @GetMapping("/deploy")
    public BaseResponse deploy(){
        Bcos3EventRegister register = bcos3EventRegisterFactory.get(generalOracleConfig.getChainId(), generalOracleConfig.getGroupId());
        Client client = register.getBcos3client();
        CryptoKeyPair keyPair = register.getKeyPair();
        String oracleCoreAddress = register.getConfig().getOracleCoreAddress();
        List<String> l = new ArrayList();
        RetCode retCode = ConstantCode.SYSTEM_EXCEPTION;
        try {
            GeneralOracleClient generalOracleClient =  new GeneralOracleClient(oracleCoreAddress,client,keyPair);
            generalOracleClient.deployContract();
            l.add(generalOracleConfig.getChainId()+":"+generalOracleConfig.getGroupId());
            l.add("oracleCoreAddress = "+oracleCoreAddress);
            l.add("dappContractAddress = "+generalOracleClient.getDappContractAddress());
        }catch (Exception e){
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add(e.getMessage());
        }
        return  BaseResponse.pageResponse(retCode,l,l.size());
    }


    GeneralOracleSource selectUrl(String urlid)
    {
        if(urlid.isEmpty()){
            return new GeneralOracleSource(
                    generalOracleConfig.getUrl(),
                    generalOracleConfig.getTimesAmount(),
                    generalOracleConfig.getReturnType()
            );
        }
        if(urlid.compareTo("1")==0)
        {
            return new GeneralOracleSource(
                    generalOracleConfig.getUrl1(),
                    generalOracleConfig.getTimesAmount1(),
                    generalOracleConfig.getReturnType1()
            );
        }
        if(urlid.compareTo("2")==0)
        {
            return new GeneralOracleSource(
                    generalOracleConfig.getUrl2(),
                    generalOracleConfig.getTimesAmount2(),
                    generalOracleConfig.getReturnType2()
            );
        }
        if(urlid.compareTo("3")==0)
        {
            return new GeneralOracleSource(
                    generalOracleConfig.getUrl3(),
                    generalOracleConfig.getTimesAmount3(),
                    generalOracleConfig.getReturnType3()
            );
        }
        return null;
    }
    @GetMapping("/get")
    public BaseResponse get(@RequestParam(value = "url", defaultValue = "") String urlid){
        Bcos3EventRegister register = bcos3EventRegisterFactory.get(generalOracleConfig.getChainId(), generalOracleConfig.getGroupId());
        Client client = register.getBcos3client();
        CryptoKeyPair keyPair = register.getKeyPair();
        String oracleCoreAddress = register.getConfig().getOracleCoreAddress();
        List<String> l = new ArrayList();
        l.add(generalOracleConfig.getChainId()+":"+generalOracleConfig.getGroupId());
        l.add("oracleCoreAddress = "+oracleCoreAddress);
        l.add("dappContractAddress = "+generalOracleConfig.getContractAddress());
        //l.add("url = "+ generalOracleConfig.getUrl());
        RetCode retCode = ConstantCode.SUCCESS;
        try {
            GeneralOracleClient generalOracleClient =  new GeneralOracleClient(oracleCoreAddress,client,keyPair);
            generalOracleClient.loadContract(generalOracleConfig.getContractAddress());
            GeneralOracleSource source = selectUrl(urlid);
            if(source==null){
                throw new Exception("Missing source for urlId : "+urlid);
            }
            log.info("Select source is {},{},{}",source.getTimesAmount(),source.getReturnType(),source.getUrl());
            l.add("url = "+ source.getUrl());

            BigInteger retValue = generalOracleClient.reqeustSource(source);
            l.add("retValue = "+ retValue);
        }catch (Exception e){
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add("ERROR : "+e.getMessage());
        }
        return  BaseResponse.pageResponse(retCode,l,l.size());
    }
}

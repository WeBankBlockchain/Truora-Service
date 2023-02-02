package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.pojo.vo.RetCode;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.dapps.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*演示的restful 入口
*
* 从默认配置的url里获取一个随机数
*    "http://localhost:5022/truora/dapps/get",
* 指定了url序号，对应application-dpps.yaml配置里 GeneralOracle段里的几个url。
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
    @Autowired private DappsConfig dappsConfig;
    @GetMapping("/deploy")
    public BaseResponse deploy(){
        String chainId = dappsConfig.getChainId();
        String groupId = dappsConfig.getGroupId();
        Bcos3EventRegister register = bcos3EventRegisterFactory.get(chainId,groupId);
        Client client = register.getBcos3client();
        CryptoKeyPair keyPair = register.getKeyPair();
        String oracleCoreAddress = register.getConfig().getOracleCoreAddress();
        List<String> l = new ArrayList();
        RetCode retCode = ConstantCode.SYSTEM_EXCEPTION;
        try {
            GeneralOracleClient generalOracleClient =  new GeneralOracleClient(oracleCoreAddress,client,keyPair);
            generalOracleClient.deployContract();
            l.add(chainId+":"+groupId);
            l.add("oracleCoreAddress = "+oracleCoreAddress);
            l.add("dappContractAddress = "+generalOracleClient.getDappContractAddress());
        }catch (Exception e){
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add(e.getMessage());
        }
        return  BaseResponse.pageResponse(retCode,l,l.size());
    }


    @GetMapping("/list")
    public BaseResponse list(){
        List  l = generalOracleConfig.getSources();
        return BaseResponse.pageResponse(ConstantCode.SUCCESS,l,l.size());

    }

    GeneralOracleSource selectUrl(String strUrlid)
    {
        if(strUrlid.isEmpty()){
            strUrlid = "0";
        }
        int urlId = Integer.parseInt(strUrlid);

        if(generalOracleConfig.getSources().size()<(urlId+1)){
            return null;
        }
        return generalOracleConfig.getSources().get(urlId);

    }
    GeneralOracleSource attachInput(GeneralOracleSource source, String input)
    {

        GeneralOracleSource newSource = new GeneralOracleSource();
        newSource.setReturnType(source.getReturnType());
        newSource.setTimesAmount(source.getTimesAmount());
        String url = source.getUrl();
        input = input.trim();
        if(!input.isEmpty()) {
            if (url.indexOf('?') > 0) {
                url = url + "&input=" + input;
            } else {
                url = url + "?input=" + input;
            }
        }
        newSource.setUrl(url);
        return newSource;
    }



    @GetMapping("/get")
    public BaseResponse get(@RequestParam(value = "url", defaultValue = "") String urlid,
                            @RequestParam(value = "input", defaultValue = "") String input,
                            @RequestParam(value = "address", defaultValue = "") String contractAddress
    ){
        String chainId = dappsConfig.getChainId();
        String groupId = dappsConfig.getGroupId();
        Bcos3EventRegister register = bcos3EventRegisterFactory.get(chainId, groupId);
        Client client = register.getBcos3client();
        CryptoKeyPair keyPair = register.getKeyPair();
        String oracleCoreAddress = register.getConfig().getOracleCoreAddress();
        List<String> l = new ArrayList();
        Map<String,Object> m = new HashMap();
        l.add(chainId+":"+groupId);
        m.put("chainId",chainId);
        m.put("groupId",groupId);
        l.add("oracleCoreAddress = "+oracleCoreAddress);
        m.put("oracleCoreAddress",oracleCoreAddress);
        //l.add("url = "+ generalOracleConfig.getUrl());
        RetCode retCode = ConstantCode.SUCCESS;
        try {
            log.info("dapp on blocknumber {}",client.getBlockNumber().getBlockNumber());
            GeneralOracleClient generalOracleClient =  new GeneralOracleClient(oracleCoreAddress,client,keyPair);

            //如果url有输入address，则使用它作为dapp的合约地址，不然再检查一次是否有配置
            if( contractAddress.isEmpty()   ){
               contractAddress = generalOracleConfig.getContractAddress().trim();
            }
            //如果没有输入address，且没有配置，就先部署一次
            if(contractAddress.isEmpty()){
                generalOracleClient.deployContract();
                log.info("deploy GeneralOracle Contract ,address: {} ",generalOracleClient.getDappContractAddress());
            }else {
                generalOracleClient.loadContract(contractAddress);
                log.info("load GeneralOracle Contract ,address: {}",generalOracleClient.getDappContractAddress());
            }
            l.add("dappContractAddress = "+generalOracleClient.getDappContractAddress());
            GeneralOracleSource selSource = selectUrl(urlid);


            if(selSource==null){
                throw new Exception("Missing source for urlId : "+urlid);
            }
            GeneralOracleSource source = attachInput(selSource,input);

            log.info("Select source is {},{},{}",source.getTimesAmount(),source.getReturnType(),source.getUrl());
            l.add("url = "+ source.getUrl());
            m.put("url",source.getUrl());
            GeneralResult retValue =  generalOracleClient.reqeustSource(source);
            l.add("result = [ "+ retValue.descriptData() + " ]");
            m.put("result",retValue);
        }catch (Exception e){
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add("ERROR : "+e.getMessage());
            m.put("error",e.getMessage());
        }
        //m.put("retCode",retCode);
        //return  BaseResponse.pageResponse(retCode,l,l.size());
        return new BaseResponse(retCode,m);
    }
}

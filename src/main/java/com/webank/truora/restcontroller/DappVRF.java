package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.pojo.vo.RetCode;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.contract.bcos3.simplevrf.RandomNumberSampleVRF;
import com.webank.truora.dapps.DappsConfig;
import com.webank.truora.vrfutils.VRFUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.crypto.vrf.Curve25519VRF;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFInterface;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/dapps", tags = "dapps vrf demo")
@Slf4j
@RestController
@RequestMapping(value = "/dapps",produces = {"application/JSON"})
public class DappVRF {
    @Autowired
    private Bcos3EventRegisterFactory bcos3EventRegisterFactory;
    @Autowired
    DappsConfig dappsConfig;
    String chainId;
    String groupId;
    Bcos3EventRegister register ;

    CryptoKeyPair vrfKeyPair;


    @PostConstruct
    private void  init() {
        chainId = dappsConfig.getChainId();
        groupId = dappsConfig.getGroupId();
        register = bcos3EventRegisterFactory.get(chainId,groupId);
        vrfKeyPair = register.getKeyPair();

    }

    public String deployByType(String vrfType) throws DecoderException, ContractException {
        Client client = register.getBcos3client();
        CryptoKeyPair keyPair = register.getKeyPair();
        String coreAddress = "";
        byte[] keyHashByte ;
        if (vrfType.compareTo("25519")==0) {
            VRFInterface vrfInterface = new Curve25519VRF();
            String vrf25519PublicKey = vrfInterface.getPublicKeyFromPrivateKey(vrfKeyPair.getHexPrivateKey());
            keyHashByte = client.getCryptoSuite().hash(Hex.decodeHex(vrf25519PublicKey));
            coreAddress = register.getConfig().getVrf25519CoreAddress();
        }else{
            keyHashByte = VRFUtils.calculateTheHashOfPK(vrfKeyPair.getHexPublicKey());
            coreAddress = register.getConfig().getVrfK1CoreAddress();
        }
        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(client,keyPair,coreAddress,keyHashByte);
        return randomNumberConsumer.getContractAddress();

    }

    @GetMapping("/vrf/deploy")
    public BaseResponse deploy(@RequestParam(value = "type", defaultValue = "25519") String vrfType
    ){
        List<String> l = new ArrayList();
        RetCode retCode = ConstantCode.SUCCESS;
        try {
            String contractAddress = deployByType(vrfType);
            l.add(chainId+":"+groupId);
            l.add("vrfType = "+ vrfType);
            l.add("dappContractAddress = "+contractAddress);
        }catch (Exception e){
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add(e.getMessage());
        }
        return  BaseResponse.pageResponse(retCode,l,l.size());
    }

    @GetMapping("/vrf/get")
    public BaseResponse get(
            @RequestParam(value = "type", defaultValue = "25519") String vrfType,
            @RequestParam(value = "address", defaultValue = "") String contractAddress
    ){
        RetCode retCode = ConstantCode.SUCCESS;
        ArrayList<String> l = new ArrayList();
        try {
            if (contractAddress.isEmpty()) {
                contractAddress = deployByType(vrfType);
            }
            Client client = register.getBcos3client();
            CryptoKeyPair keyPair = register.getKeyPair();
            RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.load(contractAddress,client,keyPair);
            int seed = (int) (Math.random() *100);
            TransactionReceipt receipt = randomNumberConsumer.requestRandomNumber(BigInteger.valueOf(seed));
            AbstractContractWorker.dealWithReceipt(receipt);
            log.info("consumer query reqId: " + receipt.getOutput());
            log.info("randomNumberConsumer.getRandomNumbe status:",receipt.getStatus());
            log.info("RandomNumberSampleVRF reqId: " + receipt.getOutput());
            Tuple1<byte[]> requestOutput =  randomNumberConsumer.getRequestRandomNumberOutput(receipt);
            byte[] requestID = requestOutput.getValue1();
            BigInteger randomValue = BigInteger.ZERO;
            int i=0;
            for(;i<6;i++) {
                Thread.sleep(1000);
                randomValue = randomNumberConsumer.getById(requestID);
                log.info("{}: Random:[{}]", i,randomValue);
                if(randomValue.compareTo(BigInteger.ZERO) != 0)
                {
                    break;
                }

            }
            l.add("vrfType = "+ vrfType);
            l.add("dappContractAddress = "+contractAddress);
            if(i>5){
                throw new Exception("Timeout");
            }
            l.add("vrf random result: {"+randomValue+"}");
        }catch(Exception e){
            log.error("Exception",e);
            retCode = ConstantCode.SYSTEM_EXCEPTION;
            l.add("ERROR : "+e.getMessage());
        }
        return  BaseResponse.pageResponse(retCode,l,l.size());

    }

}

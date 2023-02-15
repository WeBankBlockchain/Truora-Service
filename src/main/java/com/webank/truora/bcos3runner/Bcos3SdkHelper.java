package com.webank.truora.bcos3runner;

import com.webank.truora.contract.bcos3.OracleCore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.v3.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.math.BigInteger;
@Slf4j
@Data
public class Bcos3SdkHelper {
    BcosSDK sdk;

    public Bcos3SdkHelper(){}
    public Bcos3SdkHelper(String configFileName){
        init(configFileName);
    }

    public void init(String configFileName) {
        try {
            if (configFileName==null || configFileName.isEmpty()) {
                configFileName = "bcos3sdk_config.toml";
            }
            Resource r =  new ClassPathResource(configFileName);
            configFileName = r.getFile().getAbsolutePath();
            log.info("Using Bcos3SDK Configfile: {}",configFileName);
            if (configFileName == null) {
                throw new Exception("The configFile " + configFileName + " doesn't exist!");
            }
            sdk = BcosSDK.build(configFileName);
        } catch (Exception e) {
            log.error("BcosClient init error ",e);
        }
    }


    public void testClient() throws Exception{
        Client client = this.sdk.getClient("group0");
        BlockNumber blocknumber = client.getBlockNumber();
        BcosBlock block = client.getBlockByNumber(BigInteger.valueOf(1), true, true);
        System.out.println("block : " + block.getResult().toString());
        System.out.println("blocknumber is :" + blocknumber.getBlockNumber());
        CryptoKeyPair credential = client.getCryptoSuite().getCryptoKeyPair();

        OracleCore oracleCore = OracleCore.deploy(client, credential, "chain0", "group0");
        System.out.println("Deploy Oracle Core address:" + oracleCore.getContractAddress());

    }

}

package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.contract.bcos3.OracleCore;
import com.webank.truora.contract.bcos3.simplevrf.VRFCore;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.v3.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.net.URL;

public class Bcos3Client {
    @Autowired
    Bcos3ClientConfig bcos3sdkconfig;

    public void init() {
        try {
            String configFileName = "bcos3sdk_config.toml";
            URL configUrl = Bcos3Client.class.getClassLoader().getResource(configFileName);
            System.out.println("The configFile is: " + configUrl);
            if (configUrl == null) {
                System.out.println("The configFile " + configFileName + " doesn't exist!");
                return;
            }

            String groupId = "group0";
            String chainId = "chain0";
            String configFile = configUrl.getPath();
            System.out.println("configFile : " + configFile);
            BcosSDK sdk = BcosSDK.build(configFile);
            Client client = sdk.getClient(groupId);
            BlockNumber blocknumber = client.getBlockNumber();
            BcosBlock block = client.getBlockByNumber(BigInteger.valueOf(1), true, true);
            System.out.println("block : " + block.getResult().toString());
            System.out.println("blocknumber is :" + blocknumber.getBlockNumber());
            CryptoKeyPair credential = client.getCryptoSuite().getCryptoKeyPair();

            OracleCore oracleCore = OracleCore.deploy(client, credential, chainId, groupId);
            System.out.println("Deploy Oracle Core address:" + oracleCore.getContractAddress());
            VRFCore vrfCore = VRFCore.deploy(client, credential, chainId, groupId);
            System.out.println("Deploy VRF Core address:" + vrfCore.getContractAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

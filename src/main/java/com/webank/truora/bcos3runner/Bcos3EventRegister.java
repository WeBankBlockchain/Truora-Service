package com.webank.truora.bcos3runner;

import com.webank.truora.base.properties.EventRegisterConfig;
import lombok.Data;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;

@Data
public class Bcos3EventRegister {
    private EventRegisterConfig config;
    private String platform;
    private Client bcos3client;
    private CryptoKeyPair keyPair;
}

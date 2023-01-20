package com.webank.truora.crawler;

import lombok.Data;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;

@Data
public class AbstractCrawler implements ISourceCrawler {
    CryptoSuite cryptoSuite;

    @Override
    public String handle(String inputStr) throws Exception {
        return null;
    }
}

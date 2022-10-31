package com.webank.truora.bcos2runner;

import com.webank.truora.bcos2runner.base.KeyUserProperties;
import com.webank.truora.keystore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * init a private key.
 */
@Order(1)
@Component
@Slf4j
public class KeyStoreInitRunner implements ApplicationRunner {
    @Autowired
    private KeyUserProperties keyUserProperties;
    @Autowired
    private KeyStoreService keyStoreService;


    @Override
    public void run(ApplicationArguments args) {
        File keyStoreFile = FileUtils.getFile(keyUserProperties.getStore_file());
        if (!keyStoreFile.exists()) {
            log.info("keyStoreFile is not exists,create a new one now");
            keyStoreService.createKeyStore(keyStoreFile);
        }

        keyStoreService.readKeyStoreFile(keyStoreFile);

    }
}

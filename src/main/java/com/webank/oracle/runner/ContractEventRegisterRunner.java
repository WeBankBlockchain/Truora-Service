package com.webank.oracle.runner;

import java.util.List;

import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.base.properties.EventRegisterProperties;
import com.webank.oracle.transaction.oracle.OracleCoreEventCallback;
import com.webank.oracle.transaction.vrf.VRFContractEventCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ContractEventRegisterRunner {

    @Autowired private EventRegisterProperties eventRegisterProperties;
    @Autowired private ApplicationContext ctx;


    /**
     * 注册回调
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            log.info("Register event listener call back...");
            List<EventRegister> eventRegisterList = eventRegisterProperties.getEventRegisters();
            for (int i = 0; i < eventRegisterList.size(); i++) {
                EventRegister eventRegister = eventRegisterList.get(i);
                // init OracleCore on this chain and group
                OracleCoreEventCallback oracleCoreEventCallback = ctx.getBean(OracleCoreEventCallback.class, eventRegister.getChainId(), eventRegister.getGroup());
                oracleCoreEventCallback.init(eventRegister);
                log.info("OracleCore contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getOracleCoreContractAddress(), eventRegister.getChainId(), eventRegister.getGroup());

                // init VRF on this chain and group
                if (EncryptType.encryptType == EncryptType.SM2_TYPE) {
                    log.warn("VRF is not supported on FISCO-BCOS chain of SM2 type.");
                    continue;
                }
                VRFContractEventCallback vrfContractEventCallback = ctx.getBean(VRFContractEventCallback.class, eventRegister.getChainId(), eventRegister.getGroup());
                vrfContractEventCallback.init(eventRegister);
                log.info("Vrf contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getVrfContractAddress(), eventRegister.getChainId(), eventRegister.getGroup());
            }
        } catch (Exception ex) {
            log.error("ContractEventRegisterRunner exception", ex);
            System.exit(0);
        }

    }
}

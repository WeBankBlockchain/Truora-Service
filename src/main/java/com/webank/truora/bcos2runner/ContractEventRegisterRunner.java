package com.webank.truora.bcos2runner;

import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.bcos2runner.base.EventRegisterProperties;
import com.webank.truora.bcos2runner.oracle.OracleCoreEventCallback;
import com.webank.truora.bcos2runner.vrf.VRFContractEventCallback;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
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
            List<EventRegisterConfig> eventRegisterList = eventRegisterProperties.getEventRegisters();
            for (int i = 0; i < eventRegisterList.size(); i++) {
                EventRegisterConfig eventRegister = eventRegisterList.get(i);
                // init OracleCore on this chain and group
                OracleCoreEventCallback oracleCoreEventCallback = ctx.getBean(OracleCoreEventCallback.class, eventRegister.getChainId(), eventRegister.getGroupId(), eventRegister);
                oracleCoreEventCallback.init(eventRegister);
                log.info("OracleCore contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getOracleCoreAddress(), eventRegister.getChainId(), eventRegister.getGroupId());

                // init VRF on this chain and group
                if (EncryptType.encryptType == EncryptType.SM2_TYPE) {
                    log.warn("VRF is not supported on FISCO-BCOS chain of SM2 type.");
                    continue;
                }
                VRFContractEventCallback vrfContractEventCallback = ctx.getBean(VRFContractEventCallback.class, eventRegister.getChainId(), eventRegister.getGroupId(), eventRegister);
                vrfContractEventCallback.init(eventRegister);
                log.info("Vrf contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getVrfK1CoreAddress(), eventRegister.getChainId(), eventRegister.getGroupId());
            }
        } catch (Exception ex) {
            log.error("ContractEventRegisterRunner exception", ex);
            Runtime.getRuntime().exit(0);
        }
    }
}

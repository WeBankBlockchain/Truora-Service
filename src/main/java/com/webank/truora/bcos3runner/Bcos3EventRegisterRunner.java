package com.webank.truora.bcos3runner;

import com.webank.truora.bcos3runner.oracle.OracleCoreEventCallbackV3;
import com.webank.truora.bcos3runner.vrf.VRFContractEventCallbackV3;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class Bcos3EventRegisterRunner {


    @Autowired private ApplicationContext ctx;

    @Autowired private Bcos3ClientConfig bcos3config;
    @Autowired private Bcos3EventRegisterFactory eventRegisterFactory;
    /**
     * 注册回调
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            log.info("Bcos3 Register event listener call back...");
            Map<String,Bcos3EventRegister> eventRegisterList = eventRegisterFactory.getEventRegistersMapping();
            for (Map.Entry<String,Bcos3EventRegister> registerSet :eventRegisterList.entrySet()) {
                Bcos3EventRegister eventRegister = registerSet.getValue();
                // init OracleCore on this chain and group
                OracleCoreEventCallbackV3 oracleCoreEventCallback = ctx.getBean(OracleCoreEventCallbackV3.class,
                        eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), eventRegister);
                oracleCoreEventCallback.init(eventRegister);
                log.info("OracleCore contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getConfig().getOracleCoreAddress(),
                        eventRegister.getConfig().getChainId(),
                        eventRegister.getConfig().getGroupId());

                // init VRF on this chain and group
                if (EncryptType.encryptType == EncryptType.SM2_TYPE) {
                    log.warn("VRF is not supported on FISCO-BCOS chain of SM2 type.");
                    continue;
                }
                VRFContractEventCallbackV3 vrfContractEventCallback = ctx.getBean(VRFContractEventCallbackV3.class,
                        eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), eventRegister);
                vrfContractEventCallback.init(eventRegister);
                log.info("Vrf contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getConfig().getVrfCoreAddress(),
                        eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId());
            }
        } catch (Exception ex) {
            log.error("ContractEventRegisterRunner exception", ex);
            Runtime.getRuntime().exit(0);
        }
    }
}

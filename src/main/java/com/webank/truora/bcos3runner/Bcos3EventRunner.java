package com.webank.truora.bcos3runner;

import com.webank.truora.bcos3runner.oracle.OracleCoreWorker;
import com.webank.truora.bcos3runner.vrf.VRFCoreWorker;
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
public class Bcos3EventRunner {


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
            //依次初始化所有配置的事件监听服务,粒度是每个链/群组一个监听服务实例
            // 每个监听服务实例支持两种核心预言机事件 OracleCore和VRFCore
            for (Map.Entry<String,Bcos3EventRegister> registerSet :eventRegisterList.entrySet()) {
                Bcos3EventRegister eventRegister = registerSet.getValue();
                //--------------------------------------------------------
                // 1） init OracleCore on this chain and group
                OracleCoreWorker oracleWorker = ctx.getBean(OracleCoreWorker.class);
                oracleWorker.init(eventRegister);
                Bcos3EventCallback oracleCoreEventCallback = ctx.getBean(Bcos3EventCallback.class);
                oracleCoreEventCallback.init(oracleWorker);
                log.info("OracleCore contract address:[{}] of chain:[{}:{}]",
                        eventRegister.getConfig().getOracleCoreAddress(),
                        eventRegister.getConfig().getChainId(),
                        eventRegister.getConfig().getGroupId());
                //--------------------------------------------------------
                //2） init VRF on this chain and group
                if (EncryptType.encryptType == EncryptType.SM2_TYPE) {
                    log.warn("VRF is not supported on FISCO-BCOS chain of SM2 type.");
                    continue;
                }
                VRFCoreWorker vrfCoreWorker = ctx.getBean(VRFCoreWorker.class);
                vrfCoreWorker.init(eventRegister);
                Bcos3EventCallback vrfcoreEventCallback = ctx.getBean(Bcos3EventCallback.class);
                vrfcoreEventCallback.init(vrfCoreWorker);
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

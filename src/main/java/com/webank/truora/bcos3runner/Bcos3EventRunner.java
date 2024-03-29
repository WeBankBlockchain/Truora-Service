package com.webank.truora.bcos3runner;

import com.webank.truora.bcos3runner.oracle.OracleCoreWorker;
import com.webank.truora.bcos3runner.vrf.VRF25519CoreWorker;
import com.webank.truora.bcos3runner.vrf.VRFK1CoreWorker;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@ConditionalOnProperty(name = "runner.fiscobcos3",havingValue = "true")
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
                if(eventRegister.getConfig().isStartOracleCore()) {
                    OracleCoreWorker oracleWorker = ctx.getBean(OracleCoreWorker.class);
                    oracleWorker.init(eventRegister);
                    Bcos3EventCallback oracleCoreEventCallback = ctx.getBean(Bcos3EventCallback.class);
                    oracleCoreEventCallback.init(oracleWorker);
                    log.info("OracleCore contract address:[{}] of chain:[{}:{}]",
                            eventRegister.getConfig().getOracleCoreAddress(),
                            eventRegister.getConfig().getChainId(),
                            eventRegister.getConfig().getGroupId());
                }else{
                    log.info("EventRunner !SKIP! OracleCore by config");
                }
                //--------------------------------------------------------
                //2) init VRF25519  on this chain and group
                if(eventRegister.getConfig().isStartVRF25519()) {
                    VRF25519CoreWorker vrf25519CoreWorker = ctx.getBean(VRF25519CoreWorker.class);
                    vrf25519CoreWorker.init(eventRegister);
                    Bcos3EventCallback vrf25519coreEventCallback = ctx.getBean(Bcos3EventCallback.class);
                    vrf25519coreEventCallback.init(vrf25519CoreWorker);
                    log.info("Vrf 25519 contract address:[{}] of chain:[{}:{}]",
                            eventRegister.getConfig().getVrf25519CoreAddress(),
                            eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId());
                }else{
                    log.info("EventRunner !SKIP! VRF25519 by config");
                }
                //3） init VRFk1 on this chain and group
                if(eventRegister.getConfig().isStartVRFK1()) {
                    if (EncryptType.encryptType == EncryptType.SM2_TYPE) {
                        log.warn("VRF is not supported on FISCO-BCOS chain of SM2 type.");
                        continue;
                    }
                    VRFK1CoreWorker vrfCoreWorker = ctx.getBean(VRFK1CoreWorker.class);
                    vrfCoreWorker.init(eventRegister);
                    Bcos3EventCallback vrfk1coreEventCallback = ctx.getBean(Bcos3EventCallback.class);
                    vrfk1coreEventCallback.init(vrfCoreWorker);
                    log.info("Vrf contract address:[{}] of chain:[{}:{}]",
                            eventRegister.getConfig().getVrfK1CoreAddress(),
                            eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId());
                }else{
                    log.info("EventRunner !SKIP! VRFK1 by config");
                }

            }
        } catch (Exception ex) {
            log.error("ContractEventRegisterRunner exception", ex);
            Runtime.getRuntime().exit(0);
        }
    }
}

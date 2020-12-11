package com.webank.oracle.transaction.register;

import static com.webank.oracle.event.service.AbstractCoreService.dealWithReceipt;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.precompile.cns.CnsService;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.base.properties.EventRegisterProperties;
import com.webank.oracle.base.service.Web3jMapService;
import com.webank.oracle.base.utils.CredentialUtils;
import com.webank.oracle.chain.CnsMapService;
import com.webank.oracle.keystore.KeyStoreService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OracleRegisterCenterService {

    @Autowired private EventRegisterProperties eventRegisterProperties;
    @Autowired private ConstantProperties constantProperties;
    @Autowired private KeyStoreService keyStoreService;
    @Autowired private Web3jMapService web3jMapService;
    @Autowired private CnsMapService cnsMapService;

    /**
     * TODO. deploy by owner.
     *
     * 1. Auto deploy oracle register center contract if not deployed.
     * 2. Register oracle register center contract address to CNS.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // 如果没有部署 oracle register center 合约，自动部署
        // 注册到 CNS
        this.checkOrDeployOracleRegisterCenterContract();

        // 自动注册 Oracle Service 到 oracle register center
        this.registerServiceToCenter();
    }

    /**
     *
     */
    private void checkOrDeployOracleRegisterCenterContract(){
        log.info("Start to check if oracle register center deployed...");

        String nameAndVersion = constantProperties.getRegisterContractName()+":"+constantProperties.getRegisterContractVersion();
        log.info("cns NameAndVersion: [{}]", nameAndVersion);
        if (StringUtils.isBlank(nameAndVersion)) {
            log.error("Register center contract name and version not configured.");
            return;
        }

        for (EventRegister eventRegister : eventRegisterProperties.getEventRegisters()) {
            int chainId = eventRegister.getChainId();
            int groupId = eventRegister.getGroup();

            // get web3j
            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);

            // cns
            CnsService cnsService = cnsMapService.getNotNullCnsService(chainId,groupId);
            String contractAddress = null;
            try {
                contractAddress = cnsService.getAddressByContractNameAndVersion(nameAndVersion);
            } catch (Exception e) {
                log.warn("Oracle register center contract not deployed on chain:[{}:{}]", chainId, groupId);
            }
            if (StringUtils.isNotBlank(contractAddress)) {
                // oracle register center contract already deployed
                log.info("Oracle register center contract already deployed on chain:[{}:{}], address:[{}]", chainId, groupId, contractAddress);
                continue;
            }

            String oracleRegisterCenterAddress = null;
            try {
                OracleRegisterCenter oracleRegisterCenter = OracleRegisterCenter.deploy(web3j, keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER).send();
                if (StringUtils.equalsAnyIgnoreCase(Address.DEFAULT.toString(), oracleRegisterCenter.getContractAddress())) {
                    log.error("Deploy oracle register center contract on chain:[{}:{}] failed.", chainId, groupId);
                    continue;
                }
                oracleRegisterCenterAddress = oracleRegisterCenter.getContractAddress();
            } catch (Exception e) {
                log.error("Deploy oracle register center contract on chain:[{}:{}] error", chainId, groupId, e);
            } finally {
                log.info("Deploy oracle register center contract on chain:[{}:{}] success, address:[{}]", chainId, groupId, oracleRegisterCenterAddress);
            }

            try {
                cnsService.registerCns(constantProperties.getRegisterContractName(),
                        constantProperties.getRegisterContractVersion(), oracleRegisterCenterAddress, OracleRegisterCenter.ABI);
            } catch (Exception e) {
                log.error("Register oracle center to CNS on chain:[{}:{}] error", chainId, groupId, e);
            }
        }
    }

    /**
     * 在所有链上注册 oracle service
     */
    private void registerServiceToCenter(){
        log.info("Start to register this service to oracle register center ...");
        String nameAndVersion = constantProperties.getRegisterContractNameAndVersion();
        if (StringUtils.isBlank(nameAndVersion)) {
            log.error("Register center contract name and version not configured.");
            return;
        }

        for (EventRegister eventRegister : eventRegisterProperties.getEventRegisters()) {
            int chainId = eventRegister.getChainId();
            int groupId = eventRegister.getGroup();

            String operator = eventRegister.getOperator();
            String url = eventRegister.getUrl();
            List<BigInteger> publicKeyList = CredentialUtils.getPublicKeyList(this.keyStoreService.getKeyStoreInfo().getPublicKey());

            // register to center
            this.registerServiceToCenterOfChainAndGroup(chainId,groupId,operator,url,publicKeyList);
        }
    }


    /**
     * 在单链上注册 oracle service
     *
     * @param chainId
     * @param groupId
     * @param operator
     * @param url
     * @param publicKeyList
     */
    private void registerServiceToCenterOfChainAndGroup(int chainId, int groupId, String operator, String url, List<BigInteger> publicKeyList)  {
        // get web3j
        Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);

        // load oracle register center
        String registerCenterAddress = null;
        try {
            registerCenterAddress = this.getRegisterCenterAddress(chainId, groupId);
        } catch (Exception e) {
            log.error("Oracle register center not deploy on this chain and group:[{}:{}]", chainId, groupId);
            return;
        }
        log.info("Oracle register center address:[{}]", registerCenterAddress);
        OracleRegisterCenter registerCenter = OracleRegisterCenter.load(registerCenterAddress, web3j,
                this.keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER);

        try {
            // check if oracle service already register
            Boolean serviceExists = registerCenter.isOracleExist(this.keyStoreService.getKeyStoreInfo().getAddress()).send();
            if (serviceExists){
                log.info("This service is already register to oracle register center");
                return;
            }

            // register
            TransactionReceipt oracleRegisterReceipt = registerCenter.oracleRegister(operator, url, publicKeyList).send();
            dealWithReceipt(oracleRegisterReceipt);
            log.info("This service register to chain:[{}:{}] success, receipt status:[{}]", chainId, groupId, oracleRegisterReceipt.getStatus());
        } catch (Exception e) {
            log.error("This service register to chain:[{}:{}] error", chainId, groupId);
            //log.error("  error stack: {}",e.getStackTrace());
            e.printStackTrace();
        }
    }




    /**
     * @param chainId
     * @param groupId
     * @return
     * @throws Exception
     */
    public List<OracleServiceInfo> getOracleServiceList(int chainId, int groupId) throws Exception {
        String registerCenterAddress = this.getRegisterCenterAddress(chainId, groupId);
        return getOracleServiceList(registerCenterAddress, chainId, groupId);
    }

    /**
     * @param registerCenterAddress
     * @param chainId
     * @param groupId
     * @return
     * @throws Exception
     */
    public List<OracleServiceInfo> getOracleServiceList(String registerCenterAddress, int chainId, int groupId) throws Exception {
        // get web3j and credentials
        Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
        Credentials credentials = keyStoreService.getCredentials();

        // load oracle register center contract
        OracleRegisterCenter registerCenter = OracleRegisterCenter.load(registerCenterAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);

        // get all oracle service address list
        List<String> allServiceInfo = registerCenter.getAllOracleServiceInfo().send();

        if (allServiceInfo == null || CollectionUtils.isEmpty(allServiceInfo)) {
            log.error("Not oracle service registered.");
            return Collections.emptyList();
        }

        // get oracle info list by address list
        return allServiceInfo.stream().map(address -> {
            try {
                // TODO. sync to DB
                Tuple10 serviceInfo = registerCenter.getOracleServiceInfo(address).send();

                // convert output to oracle service info
                return OracleServiceInfo.build(serviceInfo);
            } catch (Exception e) {
                log.error("Get oracle service info on chain:[{}:{}] error,",
                        chainId, groupId);
                return null;
            }
        }).filter((service) -> service != null).collect(Collectors.toList());
    }

    /**
     * @param chainId
     * @param groupId
     * @return
     * @throws Exception
     */
    public String getRegisterCenterAddress(int chainId, int groupId){
        String nameAndVersion = constantProperties.getRegisterContractName()+":"+constantProperties.getRegisterContractVersion();
        if (StringUtils.isBlank(nameAndVersion)) {
            log.error("Register center contract name and version not configured.");
            throw new OracleException(ConstantCode.REGISTER_CONTRACT_NOT_CONFIGURED);
        }

        // get oracle register center address by CNS
        CnsService cnsService = cnsMapService.getNotNullCnsService(chainId,groupId);
        return cnsService.getAddressByContractNameAndVersion(nameAndVersion);
    }

}

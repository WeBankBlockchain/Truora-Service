package com.webank.truora.bcos2runner.oracle;

import com.webank.truora.base.config.ServerConfig;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.bcos2runner.base.EventRegisterProperties;
import com.webank.truora.bcos2runner.Web3jMapService;
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.bcos2runner.base.CnsMapService;
import com.webank.truora.contract.bcos2.OracleRegisterCenter;
import com.webank.truora.keystore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.precompile.cns.CnsService;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.webank.truora.bcos2runner.AbstractCoreService.dealWithReceipt;

@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@Service
@Slf4j

public class OracleRegisterCenterService {

    @Autowired private EventRegisterProperties eventRegisterProperties;
    @Autowired private ConstantProperties constantProperties;
    @Autowired private KeyStoreService keyStoreService;
    @Autowired private Web3jMapService web3jMapService;
    @Autowired private CnsMapService cnsMapService;
    @Autowired private ServerConfig serverConfig;

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

        for (EventRegisterConfig eventRegister : eventRegisterProperties.getEventRegisters()) {
            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();

            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            String contractAddress = null;
                    
            try {
                 contractAddress = getRegisterCenterAddress(chainId, groupId);
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

            CnsService cnsService = cnsMapService.getNotNullCnsService(chainId,groupId);

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

        for (EventRegisterConfig eventRegister : eventRegisterProperties.getEventRegisters()) {
            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();

            String operator = serverConfig.getOperator();
            String url = serverConfig.getUrl();
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
    private void registerServiceToCenterOfChainAndGroup(String chainId, String groupId, String operator, String url, List<BigInteger> publicKeyList)  {
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
            log.info("Register service:[{}] to chain:[{}:{}]", this.keyStoreService.getKeyStoreInfo().getAddress(), chainId, groupId);

            // register
            TransactionReceipt oracleRegisterReceipt = registerCenter.oracleRegister(operator, url, publicKeyList).send();
            dealWithReceipt(oracleRegisterReceipt);
            log.info("This service:[{}] register to chain:[{}:{}] success, receipt status:[{}]",
                    this.keyStoreService.getKeyStoreInfo().getAddress(), chainId, groupId, oracleRegisterReceipt.getStatus());
        } catch (Exception e) {
            log.error("This service register to chain:[{}:{}] error", chainId, groupId, e);
        }
    }




    /**
     * @param chainId
     * @param groupId
     * @return
     * @throws Exception
     */
    public List<OracleServiceInfo> getOracleServiceList(String chainId, String groupId) throws Exception {
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
    public List<OracleServiceInfo> getOracleServiceList(String registerCenterAddress, String chainId, String groupId) throws Exception {
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
    public String getRegisterCenterAddress(String chainId, String groupId){
        String nameAndVersion = constantProperties.getRegisterContractName()+":"+constantProperties.getRegisterContractVersion();
        if (StringUtils.isBlank(nameAndVersion)) {
            log.error("Register center contract name and version not configured.");
            throw new OracleException(ConstantCode.REGISTER_CONTRACT_NOT_CONFIGURED);
        }

        // get oracle register center address by CNS
        CnsService cnsService = cnsMapService.getNotNullCnsService(chainId,groupId);
        return cnsService.getAddressByContractNameAndVersion(nameAndVersion);
    }

    //todo
    public void updateOracleInfo(String operatorInfo, String url)  {

        for (EventRegisterConfig eventRegister : eventRegisterProperties.getEventRegisters()) {

            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();

            String contractAddress = getRegisterCenterAddress(chainId, groupId);
            if (StringUtils.isBlank(contractAddress)) {
                // oracle register center contract already deployed
                log.info("there is no registerCenter contract chain:[{}:{}], address:[{}]", chainId, groupId, contractAddress);
                continue;
            }

            Credentials credentials = keyStoreService.getCredentials();

            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);

            // load oracle register center contract
            OracleRegisterCenter registerCenter = OracleRegisterCenter.load(contractAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);

            // todo
            try {
            Tuple10 allServiceInfo = registerCenter.getOracleServiceInfo(credentials.getAddress()).send();
            String oldOpreator = (String)allServiceInfo.getValue5();
            String oldUrl =(String) allServiceInfo.getValue6();
            List<BigInteger> publicKeyList = CredentialUtils.getPublicKeyList(this.keyStoreService.getKeyStoreInfo().getPublicKey());

           String newOperatorInfo = operatorInfo == null? oldOpreator: operatorInfo;
           String newUrl = url == null? oldUrl: url;

            registerCenter.updateOracleInfo( publicKeyList, newOperatorInfo, newUrl).send();
            } catch (Exception e) {
                throw new OracleException(ConstantCode.ORACLE_REGISTER_UPDATE_INFO);
            }
        }
    }
}

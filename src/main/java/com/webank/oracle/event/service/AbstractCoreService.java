package com.webank.oracle.event.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;

import com.webank.oracle.base.enums.ContractTypeEnum;
import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.service.Web3jMapService;
import com.webank.oracle.base.utils.ChainGroupMapKeyUtil;
import com.webank.oracle.base.utils.DecodeOutputUtils;
import com.webank.oracle.contract.ContractDeploy;
import com.webank.oracle.contract.ContractDeployRepository;
import com.webank.oracle.event.vo.BaseLogResult;
import com.webank.oracle.http.HttpService;
import com.webank.oracle.keystore.KeyStoreService;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public abstract class AbstractCoreService {

    /**
     * 链 ID 和群组对应的合约地址
     */
    protected Map<String, String> contractAddressMap = new ConcurrentHashMap<>();

    @Autowired protected Web3jMapService web3jMapService;
    @Autowired protected KeyStoreService keyStoreService;
    @Autowired protected HttpService httpService;
    @Autowired protected ContractDeployRepository contractDeployRepository;

    /**
     * 返回当前合约类型
     *
     * @return
     */
    public abstract ContractTypeEnum getContractType();


    /**
     * 检查合约地址是否有效
     *
     * @return
     */
    public abstract boolean isContractAddressValid(int chainId, int groupId, String contractAddress);

    /**
     * 部署合约
     *
     * @return
     */
    protected abstract String deployContract(int chainId, int group);

    /**
     * 获取结果并上链
     *
     * @return
     */
    public abstract String getResultAndUpToChain(int chainId, int groupId, BaseLogResult baseLogResult) throws Exception;

    /**
     * @param chainId
     * @param groupId
     * @param contractAddress
     * @param baseLogResult
     * @param result
     * @return
     */
    public abstract void fulfill(int chainId, int groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception;


    /**
     * 加载合约地址
     *
     * @return
     */
    public String loadContractAddress(int chainId, int groupId) {
        ContractTypeEnum contractType = getContractType();

        // load from db
        Optional<ContractDeploy> deployOptional =
                this.contractDeployRepository.findByChainIdAndGroupIdAndContractType(chainId, groupId, contractType.getId());
        ContractDeploy contractDeploy = null;
        if (deployOptional.isPresent()) {
            contractDeploy = deployOptional.get();
            String contractAddress = contractDeploy.getContractAddress();
            if (StringUtils.isNotBlank(contractAddress)) {
                // contract address valid
                if (this.isContractAddressValid(chainId, groupId, contractAddress)) {
                    // oracle core already deployed
                    contractAddressMap.put(ChainGroupMapKeyUtil.getKey(chainId, groupId), contractAddress);
                    return contractAddress;
                }

                // delete this dirty contract address
                log.warn("Contract address:[{}] exists, but not valid on chain:[{}] and group:[{}]. " +
                                "Maybe dirty data, try to re-deploy this contract:[{}].",
                        contractAddress, chainId, groupId, this.getContractType().getType());
                log.warn("Delete contract address:[{}:{}]", contractDeploy.getChainId(), contractAddress);
                this.contractDeployRepository.deleteById(contractDeploy.getId());
            }
        }

        // deploy contract
        log.info("Deploy contract:[{}] on chain:[{}:{}]", this.getContractType(), chainId, groupId);
        String deployedContractAddress = this.deployContract(chainId, groupId);
        if (StringUtils.isNotBlank(deployedContractAddress)) {
            contractDeploy = ContractDeploy.build(chainId, groupId, contractType);
            contractDeploy.setContractAddress(deployedContractAddress);
            contractDeployRepository.save(contractDeploy);
            contractAddressMap.put(ChainGroupMapKeyUtil.getKey(chainId, groupId), deployedContractAddress);
        }
        return deployedContractAddress;
    }

    /**
     * @param receipt
     */
    public static void dealWithReceipt(TransactionReceipt receipt) {
        log.info("receipt:[{}]", receipt.getOutput());
        if ("0x16".equals(receipt.getStatus()) && receipt.getOutput().startsWith("0x08c379a0")) {
            log.error("transaction error:[{}]", DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(), DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
        }
        if (!"0x0".equals(receipt.getStatus())) {
            log.error("transaction error, status:{} output:{}", receipt.getStatus(), receipt.getOutput());
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(), DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
        }
    }


}
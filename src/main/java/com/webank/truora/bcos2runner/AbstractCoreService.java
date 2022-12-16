package com.webank.truora.bcos2runner;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.exception.FullFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.ChainGroupMapUtil;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.base.utils.DecodeOutputUtils;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.database.DBContractDeployRepository;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import com.webank.truora.httputil.HttpService;
import com.webank.truora.keystore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static com.webank.truora.base.enums.ReqStatusEnum.UNSUPPORTED_RETURN_TYPE_ERROR;

/**
 *
 */

@Slf4j
public abstract class AbstractCoreService {

    @Autowired protected Web3jMapService web3jMapService;
    @Autowired protected KeyStoreService keyStoreService;
    @Autowired protected HttpService httpService;
    @Autowired protected DBContractDeployRepository contractDeployRepository;
    protected String platform = "fiscobcos";
    /**
     * 返回当前合约类型
     *
     * @return
     */
    public abstract ContractEnum getContractType();


    /**
     * 检查合约地址是否有效
     *
     * @return
     */
    public abstract boolean isContractAddressValid(String chainId, String groupId, String contractAddress);

    /**
     * 部署合约
     *
     * @return
     */
    protected abstract String deployContract(String chainId, String group);

    /**
     * 获取结果并上链
     *
     * @return
     */
    public abstract String getResultAndUpToChain(String chainId, String groupId, BaseLogResult baseLogResult) throws Exception;

    /**
     * @param chainId
     * @param groupId
     * @param contractAddress
     * @param baseLogResult
     * @param result
     * @return
     */
    public abstract void fulfill(String chainId, String groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception;


    /**
     * 加载合约地址
     *
     * @return
     */
    public String loadContractAddress(String chainId, String groupId, String currentVersion) {
        ContractEnum contractType = getContractType();

        // load from db
        Optional<DBContractDeploy> deployOptional = this.contractDeployRepository
                .findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                        platform,
                        chainId, groupId, contractType.getType(), currentVersion);

        DBContractDeploy contractDeploy = null;
        if (deployOptional.isPresent()) {
            contractDeploy = deployOptional.get();
            String contractAddress = contractDeploy.getContractAddress();
            if (StringUtils.isNotBlank(contractAddress)) {
                // contract address valid
                if (this.isContractAddressValid(chainId, groupId, contractAddress)) {
                    // oracle core already deployed
                    ChainGroupMapUtil.put(chainId, groupId, contractAddress, contractDeploy.getVersion());
                    return contractAddress;
                }

                // delete this dirty contract address
                log.warn("Contract address:[{}] and version:[{}] exists, but not valid on chain:[{}] and group:[{}]. " +
                                "Maybe dirty data, try to re-deploy this contract:[{}].",
                        contractAddress, currentVersion, chainId, groupId, this.getContractType().getName());

                // delete this contract address
                log.warn("Delete contract address:[{}:{}]", contractDeploy.getChainId(), contractAddress);
                this.contractDeployRepository.deleteById(contractDeploy.getId());
            }
        }

        // not found in database , deploy contract
        log.info("Deploy contract:[{}] of version:[{}] on chain:[{}:{}]", this.getContractType(), currentVersion, chainId, groupId);
        String deployedContractAddress = this.deployContract(chainId, groupId);
        if (StringUtils.isNotBlank(deployedContractAddress)) {
            contractDeploy = DBContractDeploy.build(platform,chainId, groupId, contractType,getContractType().getName(), currentVersion);
            contractDeploy.setContractAddress(deployedContractAddress);
            contractDeployRepository.save(contractDeploy);

            ChainGroupMapUtil.put(chainId, groupId, deployedContractAddress, currentVersion);
        }
        return deployedContractAddress;
    }
    public byte[] convertReturnBytes(ReturnTypeEnum returnTypeEnum, Object result, BigInteger timesAmount)
    {
        //ReturnTypeEnum.get(eventResponse.returnType)
        byte[] bytesValue = null;
        switch (returnTypeEnum) {
            case INT256:
                BigInteger afterTimesAmount = new BigDecimal(String.valueOf(result))
                        .multiply(new BigDecimal(timesAmount))
                        .toBigInteger();
                log.info("After times amount:[{}]", Hex.encodeHexString(afterTimesAmount.toByteArray()));
                bytesValue = CryptoUtil.toBytes(afterTimesAmount);
                break;
            case STRING:
                bytesValue = String.valueOf(result).getBytes();
                break;
            case BYTES:
                bytesValue = CryptoUtil.toBytes(result);
                break;
            default:
                throw new FullFillException(UNSUPPORTED_RETURN_TYPE_ERROR, returnTypeEnum);
        }
        return bytesValue;
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
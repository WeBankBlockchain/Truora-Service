package com.webank.truora.bcos3runner;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.ChainGroupMapUtil;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.database.DBContractDeployRepository;
import com.webank.truora.httputil.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.codec.decode.RevertMessageParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 *
 */

@Slf4j
public abstract class AbstractContractClientV3 {

    @Autowired protected Bcos3SdkFactory sdkFactory;
    @Autowired protected Bcos3ClientConfig clientConfig;
    @Autowired protected HttpService httpService;
    @Autowired protected DBContractDeployRepository contractDeployRepository;

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
    public abstract boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress);

    /**
     * 部署合约
     *
     * @return
     */
    protected abstract String deployContract(Bcos3EventRegister eventRegister);

    /**
     * 获取结果并上链
     *
     * @return
     */
    //public abstract String getResultAndUpToChain(Bcos3EventRegister eventRegister, List<String> eventDataList) throws Exception;

    /**

     * @param contractAddress
     * @param baseLogResult
     * @param result
     * @return
     */
    //public abstract void fulfill(Bcos3EventRegister eventRegister, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception;


    /**
     * 加载合约地址
     *
     * @return
     */
    public String loadOrDeployContract(Bcos3EventRegister eventRegister, String currentVersion) {

        String chainId = eventRegister.getConfig().getChainId();
        String groupId = eventRegister.getConfig().getGroupId();
        ContractEnum contractType = getContractType();

        // load from db
        Optional<DBContractDeploy> deployOptional = this.contractDeployRepository
                .findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                        clientConfig.getPlatform(),
                        chainId, groupId, contractType.getType(), currentVersion);

        DBContractDeploy contractDeploy = null;
        if (deployOptional.isPresent()) {
            contractDeploy = deployOptional.get();
            String contractAddress = contractDeploy.getContractAddress();
            if (StringUtils.isNotBlank(contractAddress)) {
                // contract address valid
                if (this.isContractAddressValid(eventRegister, contractAddress)) {
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
        String deployedContractAddress = this.deployContract(eventRegister);
        if (StringUtils.isNotBlank(deployedContractAddress)) {
            contractDeploy = DBContractDeploy.build(clientConfig.getPlatform(),
                    chainId, groupId, contractType,getContractType().getName(), currentVersion);
            contractDeploy.setContractAddress(deployedContractAddress);
            contractDeployRepository.save(contractDeploy);

            ChainGroupMapUtil.put(chainId, groupId, deployedContractAddress, currentVersion);
        }
        return deployedContractAddress;
    }

    /**
     * @param receipt
     */
    public static void dealWithReceipt(TransactionReceipt receipt) {
        log.info("receipt:[{}]", receipt.getOutput());
        if (receipt.getStatus()==0x16 ) {
            Tuple2<Boolean, String> res0x16 =  RevertMessageParser.tryResolveRevertMessage(receipt);
            log.error("dealWithReceipt 0x16 {}:{}",res0x16.getValue1(),res0x16.getValue2());
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(),
                    String.format("status:%d,code:%s,msg:%s",receipt.getStatus(), res0x16.getValue1(),res0x16.getValue2()));
        }
        if (receipt.getStatus()!=0) {

            log.error("transaction error, status:{} output:{}", receipt.getStatus(), receipt.getOutput());
            Tuple2<Boolean, String> res0x16 =  RevertMessageParser.tryResolveRevertMessage(receipt);
            log.error("dealWithReceipt 0x16 {}:{}",res0x16.getValue1(),res0x16.getValue2());;
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(),
                    String.format("status:%d,res:%s,msg:%s",receipt.getStatus(),res0x16.getValue1(),res0x16.getValue2()));
        }
    }


}
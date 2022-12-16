package com.webank.truora.bcos2runner;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 */

@Slf4j
@Service
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
public class Web3jMapService {

    @Autowired
    @Qualifier("web3jMap")
    protected Map<String,Map<String, Web3j>> web3jMap;


    /**
     * Get web3j by chainId and groupId.
     */
    public Web3j getWeb3j(String chainId, String groupId) {
        Map<String, Web3j> web3jGroupMap = web3jMap.get(chainId);
        if (MapUtils.isEmpty(web3jGroupMap) || web3jGroupMap.get(groupId) == null){
            // no web3j for chainId and groupId
            return null;
        }
        return web3jGroupMap.get(groupId);
    }

    /**
     *
     * @param chainId
     * @param groupId
     * @return
     */
    public Web3j getNotNullWeb3j(String chainId, String groupId) {
        Web3j web3j = getWeb3j(chainId, groupId);
        if (web3j == null){
            throw new OracleException(ConstantCode.GROUP_ID_NOT_EXIST,String.format("chain %s:group %s",chainId,groupId));
        }
        return web3j;
    }

}
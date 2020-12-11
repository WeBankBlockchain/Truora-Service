package com.webank.oracle.base.service;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.vo.ConstantCode;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@Service
public class Web3jMapService {

    @Autowired protected Map<Integer, Map<Integer, Web3j>> web3jMap;


    /**
     * Get web3j by chainId and groupId.
     */
    public Web3j getWeb3j(int chainId, int groupId) {
        Map<Integer, Web3j> web3jGroupMap = web3jMap.get(chainId);
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
    public Web3j getNotNullWeb3j(int chainId, int groupId) {
        Web3j web3j = getWeb3j(chainId, groupId);
        if (web3j == null){
            throw new OracleException(ConstantCode.GROUP_ID_NOT_EXIST);
        }
        return web3j;
    }

}
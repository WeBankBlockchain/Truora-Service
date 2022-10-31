package com.webank.truora.bcos2runner.base;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos2runner.Web3jMapService;
import com.webank.truora.base.utils.ChainGroupMapUtil;
import com.webank.truora.keystore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.precompile.cns.CnsService;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */

@Slf4j
@Service
public class CnsMapService {

    @Autowired private KeyStoreService keyStoreService;
    @Autowired private Web3jMapService web3jMapService;

    /**
     * All cns service instances for chain and group.
     */
    private static final Map<String, CnsService> CNS_MAP = new ConcurrentHashMap<>();


    /**
     *
     * @param chainId
     * @param groupId
     * @return
     */
    public CnsService getCnsService(String chainId, String groupId) {

        String key = ChainGroupMapUtil.getKey(chainId, groupId);
        CnsService cnsService = CNS_MAP.get(key);
        if (cnsService == null) {
            // new cns service
            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            if (web3j == null) {
                throw new OracleException(ConstantCode.CHAIN_OR_GROUP_ID_PARAM_ERROR);
            }

            // get oracle register center address by CNS
            cnsService = new CnsService(web3j, keyStoreService.getCredentials());
            CNS_MAP.putIfAbsent(key, cnsService);
        }
        return cnsService;
    }

    /**
     *
     * @param chainId
     * @param groupId
     * @return
     */
    public CnsService getNotNullCnsService(String chainId, String groupId) {
        CnsService cnsService = this.getCnsService(chainId,groupId);
        if (cnsService == null){
            throw new OracleException(ConstantCode.NO_CNS_SERVICE_ERROR);
        }
        return cnsService;
    }

}
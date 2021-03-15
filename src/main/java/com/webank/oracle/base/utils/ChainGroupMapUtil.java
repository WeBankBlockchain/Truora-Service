package com.webank.oracle.base.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class ChainGroupMapUtil {

    /**
     * 保存链，群组上 合约的地址以及对应的版本号
     *
     * Map<ChainId_GroupId, Map<ContractAddress, Version>
     */
    private static final Map<String, Map<String, String>> CONTRACT_ADDRESS_VERSION_MAP = new ConcurrentHashMap<>();

    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public static String getKey(int chainId, int groupId) {
        return String.format("%s_%s", chainId, groupId);
    }

    /**
     *  添加链，群组，合约地址和对应的版本号
     *
     * @param chainId
     * @param groupId
     * @param contractAddress
     * @param contractVersion
     */
    public static void put(int chainId, int groupId, String contractAddress, String contractVersion){
        String key = getKey(chainId,groupId);

        Map<String, String> addressVersionMap = CONTRACT_ADDRESS_VERSION_MAP.get(key);
        if (addressVersionMap == null){
            addressVersionMap = new ConcurrentHashMap<>();
            CONTRACT_ADDRESS_VERSION_MAP.put(key,addressVersionMap);
        }
        addressVersionMap.putIfAbsent(contractAddress,contractVersion);
    }

    /**
     *  获取合约的版本号
     *
     * @param chainId
     * @param groupId
     * @param contractAddress
     * @param defaultVersion
     */
    public static String getVersionWithDefault(int chainId, int groupId, String contractAddress, String defaultVersion){
        String key = getKey(chainId,groupId);

        Map<String, String> addressVersionMap = CONTRACT_ADDRESS_VERSION_MAP.get(key);
        if (addressVersionMap == null){
            return defaultVersion;
        }
        if (addressVersionMap.containsKey(contractAddress)){
            return addressVersionMap.get(contractAddress);
        }
        return defaultVersion;
    }
}
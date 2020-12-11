package com.webank.oracle.base.utils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class ChainGroupMapKeyUtil {

    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public static String getKey(int chainId, int groupId) {
        return String.format("%s_%s", chainId, groupId);
    }


}
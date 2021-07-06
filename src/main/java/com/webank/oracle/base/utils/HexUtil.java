package com.webank.oracle.base.utils;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class HexUtil {

    /**
     *
     * @param origin
     * @return
     */
    public static String add0xPrefix(String origin){
        if(StringUtils.isEmpty(origin)){
            return origin;
        }

        if (StringUtils.startsWith(origin,"0x")
            || StringUtils.startsWith(origin,"0X")){
            return origin;
        }
        return String.format("0x%s",origin);
    }

}
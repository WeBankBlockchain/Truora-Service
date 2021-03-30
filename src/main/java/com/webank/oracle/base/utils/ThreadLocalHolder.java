package com.webank.oracle.base.utils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class ThreadLocalHolder {
    /**
     * 记录请求开始时间
     */
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    /**
     * VRF: 实际计算随机数种子
     */
    private static final ThreadLocal<String> VRF_ACTUAL_SEED = new ThreadLocal<>();

    /**
     * VRF: 随机数结果
     */
    private static final ThreadLocal<String> VRF_RANDOMNESS = new ThreadLocal<>();

    public static long setStartTime(){
        long start = System.currentTimeMillis();
        START_TIME.set(start);
        return start;
    }

    public static long getStartTime(){
        Long start = START_TIME.get();
        if (start == null){
            return 0L;
        }
        return start;
    }

    public static void removeStartTime(){
        START_TIME.remove();
    }

    public static void setActualSeed(String actualSeed){
        VRF_ACTUAL_SEED.set(actualSeed);
    }

    public static String getActualSeed(){
        return VRF_ACTUAL_SEED.get();
    }

    public static void removeActualSeed(){
        VRF_ACTUAL_SEED.remove();
    }

    public static void setRandomness(String randomness){
        VRF_RANDOMNESS.set(randomness);
    }

    public static String getRandomness(){
        return VRF_RANDOMNESS.get();
    }

    public static void removeRandomness(){
        VRF_RANDOMNESS.remove();
    }
}
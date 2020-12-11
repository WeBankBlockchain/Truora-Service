package com.webank.oracle.base.utils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class ThreadLocalHolder {
    /**
     * 存储处理异常后的信息
     */
    public static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();


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

}
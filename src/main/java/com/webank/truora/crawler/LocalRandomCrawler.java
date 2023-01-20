package com.webank.truora.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*一个本地生成伪随机数的桩，主要用来做测试*/
@Slf4j
@Component("LocalRandomCrawler")
public class LocalRandomCrawler extends AbstractCrawler {
    public static String NAME = "LocalRandom";
    @Override
    public String handle(String inputStr) throws Exception {
        int max  =10000;
        int r = (int)(Math.random() * 100000000 % max);
        log.info("LocalRandomCrawler rand: {}",r);
        return String.valueOf(r);
    }
}

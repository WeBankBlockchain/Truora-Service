package com.webank.truora.crawler;
//接口类，参见SourceCrawlerFactory的注释
public interface ISourceCrawler {
    public String handle(String inputStr) throws Exception;
}

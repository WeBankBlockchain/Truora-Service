package com.webank.truora.crawler;

import lombok.Data;

@Data
public class HashUrl {
    /*这个类对应json {"name":"HashUrl","url":"http://www.xx.com/"}, 在json字符串反射到类时，必须有默认构造函数
    * 如果定义了带参数的构造函数，则需要显式的写一个默认的构造函数
    *  */
    public HashUrl(){
    }
    public HashUrl(String url){
        this.url = url;
    }
    String name = "HashUrl";
    String url ="";
}

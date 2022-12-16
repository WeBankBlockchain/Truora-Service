package com.webank.truora.crawler;

import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.httputil.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Slf4j
@Service("URLCrawler")
@Scope("prototype")
public class URLCrawler implements  ISourcCrawler{
    @Autowired
    protected HttpService httpService;

    @Override
    public String handle(String inputStr) throws Exception {
        /*传入的是简单的url或baseUrl的json*/
        String targetUrl = inputStr;
        if(JsonUtils.isJson(inputStr))
        {
            BaseUrl urlobj = JsonUtils.toJavaObject(inputStr,BaseUrl.class);
            if(urlobj.getUrl().isEmpty()){
                log.error("URLCrawler ,missing url from json : {}",inputStr);
            }
            targetUrl = urlobj.getUrl();
        }
        return dealFormatUrl(targetUrl);
    }



    public String dealFormatUrl(String url) throws Exception {
        // Samples:
        // url = "https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new"
        // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
        url = url.trim();
        int len = url.length();
        if (url.startsWith("\"")) {
            url = url.substring(1, len - 1);
            len = len-2;
        }
        int left = url.indexOf("(");
        String format ="";
        String httpUrl = "";
        String path = "";
        if(left>0) {
            int right = url.indexOf(")");
            format = url.substring(0, left);
            httpUrl = url.substring(left + 1, right);
            path = "";
            if (url.length() > right + 1) {
                path = url.substring(right + 1, len);
            }
            log.info("***parse event url resut: {}, formate: {}, path: {}", url, format, path);
        }else{
            httpUrl = url;
            format="plain";
        }
        //get data
        String finalResult =  httpService.getHttpResultAndParse(httpUrl, format, path);
        //String finalResult = "1234";
        return  finalResult;
    }



}

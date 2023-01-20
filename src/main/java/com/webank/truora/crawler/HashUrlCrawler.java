package com.webank.truora.crawler;

import com.webank.truora.base.utils.HttpUtil;
import com.webank.truora.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Slf4j
@Service("HashUrlCrawler")
@Scope("prototype")
/*通过url访问外部网页，将返回的资源内容全部打成hash*/
public class HashUrlCrawler extends AbstractCrawler {
    public static String NAME = "HashUrl";
    public String handle(String jsonInput) throws Exception{
        BaseUrl hashUrl = JsonUtils.toJavaObject(jsonInput,BaseUrl.class);
        String url = hashUrl.getUrl();
        String result = "";
        String httpResponse  = HttpUtil.get(url);
        result = cryptoSuite.hash(httpResponse);
        return result;
    }
}

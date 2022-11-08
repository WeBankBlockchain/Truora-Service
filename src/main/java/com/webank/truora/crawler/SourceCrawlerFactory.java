package com.webank.truora.crawler;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SourceCrawlerFactory {
   @Autowired
    ApplicationContext ctx;
    String suffix="Crawler";
    public String handle(String inputStr) throws Exception{

        Map<String ,Object> inputMap = JsonUtils.toMap(inputStr);

        if(!inputMap.containsKey("name")){
            throw new OracleException(ConstantCode.PARAM_EXCEPTION,"SourceCrawlerFactory can't found the crawlername :"+inputStr);
        }
        String name = (String)inputMap.get("name");
        return handle(name,inputStr);

    }

    public String handle(String name,String inputStr) throws Exception{
        String result ="";
        String fullHandlerName = name+suffix;
        ISourcCrawler handler = (ISourcCrawler)ctx.getBean(fullHandlerName);
        if(handler == null){
            throw new OracleException(ConstantCode.PARAM_EXCEPTION,"Event handle exception , can't found the full handle name :"+fullHandlerName);
        }
        result = handler.handle(inputStr);
        return result;

    }

}


package com.webank.truora.crawler;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/*设计一个简单的插件化机制，便于在扩展更多的的资源获取方式时，对接到事件回调流程里
 * 即：EventWorker收到链上预言机事件，则调用这里的接口，传入事件里和数据源有关的参数
 * Factory根据名称等信息，确定实例化哪一个bean去调用外部资源，如“UrlCrawler”去获取，并返回结果，供worker去上链
 * 初步拟定进出的参数都是简单的字符串，可以打成json格式
 * * 如{name="Url",url="http://www.xyz.com"},
 * 只需要包含有name，则可以定位到bean，然后json的解析可以由bean自由发挥。
 *规则： bean的名字必须是 [XXX]Crawler, 如URLCrawler,注解要指明名字： @Service("URLCrawler") ，可选：@Scope("prototype")
 *
 * 示例:
 * @Autowired
 * SourceCrawlerFactory sourceCrawlerFactory;
 * 简单传入jsonStr
 * finalResult = sourceCrawlerFactory.handle(jsonStr);
 * 显式指向名字
 * finalResult = sourceCrawlerFactory.handle("URL",url);
 **
 * 此版本的设计可以满足简单的数据源，比较泛型，但问题是描述性较差
 *
 * 注意： eventWorker里已经使用了线程池，这里的接口调用应该是简单且同步的，否则要加入future等异步机制。
 *
 * *todo：后续根据具体的数据源情况，丰富和清晰化参数
 * *todo：做成定期任务，支持一次监听，定期获取外部数据源并上链刷新的模式，并支持暂停、更改周期、终止等。
 *        这样要感知链客户端信息，另外设计接口
 * */
@Slf4j
@Component
public class SourceCrawlerFactory {
    @Autowired
    ApplicationContext ctx;
    String FIXED_SUFFIX = "Crawler";

    public String handle(String inputStr) throws Exception {

        Map<String, Object> inputMap = JsonUtils.toMap(inputStr);
        if(inputMap == null){
            throw new OracleException(ConstantCode.PARAM_EXCEPTION, "SourceCrawlerFactory input is empty: [" + inputStr+"]");
        }
        if (!inputMap.containsKey("name")) {
            throw new OracleException(ConstantCode.PARAM_EXCEPTION, "SourceCrawlerFactory can't found the crawlername :" + inputStr);
        }
        String name = (String) inputMap.get("name");
        return handle(name, inputStr);

    }

    public String handle(String name, String inputStr) throws Exception {
        String result = "";
        String fullHandlerName = name.trim() ;
        /*如果没有带上后缀Crawler，则加上，如果已经带了，就不需要再加
        * 方便应用写关键字，如“URL”等效于"URLCrawler"
        * */
        if  (!fullHandlerName.endsWith(FIXED_SUFFIX)){
            fullHandlerName =fullHandlerName + FIXED_SUFFIX;
        }
        ISourcCrawler crawler = null;

        try {
            crawler = (ISourcCrawler) ctx.getBean(fullHandlerName);
        } catch (Exception e) {
            log.error("SourceCrawlerFactory getBean error {}", fullHandlerName, e);
        } finally {
            if (crawler == null) {
                throw new OracleException(ConstantCode.PARAM_EXCEPTION, "SourceCrawlerFactory  can't found the full handle name :" + fullHandlerName);
            }
        }
        result = crawler.handle(inputStr);
        return result;

    }

}


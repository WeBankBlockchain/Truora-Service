package com.webank.truora.httputil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.JsonPath;
import com.webank.truora.base.enums.ReqStatusEnum;
import com.webank.truora.base.exception.JsonParseException;
import com.webank.truora.base.exception.RemoteCallException;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.webank.truora.base.utils.JsonUtils.toList;

/**
 * service for http request.
 */
@Slf4j
@Service
public class HttpService {

    @Autowired private ConstantProperties constantProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        HttpUtil.init(constantProperties.getConnectTimeout(),
                constantProperties.getReadTimeout(), constantProperties.getWriteTimeout());

    }


    /**
     * get result by url,and get value from the result by keyList.
     *
     * @param url
     * @param format
     * @param path
     * @return
     * @throws Exception
     */
    public static String getHttpResultAndParse(String url, String format, String path) throws Exception {
        try {
            //get data
            String result = HttpUtil.get(url);
            String value = "";
            // fetch value from result by format
            switch (StringUtils.lowerCase(format)) {
                case "json":
                    String jsonpath = "$"+path;
                    value = JsonPath.parse(result).read(jsonpath,String.class);
                    if (value == null) {
                        throw new JsonParseException(ReqStatusEnum.RESULT_FORMAT_ERROR, format, result);
                    }
                    break;
                case "plain":
                    try {
                        //text/plain
                        if (path.equals("")) {
                            value = result.split("\n")[0];
                        } else {
                            int left = path.indexOf("[");
                            int right = path.indexOf("]");
                            int index = Integer.parseInt(path.substring(left+1, right));
                            value = result.split("\n")[index];
                        }
                    } catch (Exception e) {
                        throw new JsonParseException(ReqStatusEnum.RESULT_FORMAT_ERROR, format, result);
                    }
                default:{
                    return result;
                }

            }
            return value;
        } catch (Exception e) {
            log.error("Request url:[{}] unexpected exception error.", url, e);
            throw e;
        }
    }

    /**
     * get value from object by keyList.
     *
     * @param jsonNode
     * @param keyList
     * @return
     */
    private static Object getValueByKeys(JsonNode jsonNode, List<String> keyList) {
        if (jsonNode == null || keyList == null || keyList.size() == 0) return jsonNode;
        Object finalResult = jsonNode;
        for (String key : keyList) {
            finalResult = getValueByKey(finalResult, key);
        }
        return finalResult;
    }


    /**
     * get value by key.
     *
     * @param jsonNode
     * @param key
     * @return
     */
    private static Object getValueByKey(Object jsonNode, String key) {
        if (jsonNode instanceof ArrayNode) {
            List<Object> jsonArray = toList(jsonNode);
            return jsonArray.get(Integer.valueOf(String.valueOf(key)));
        }
        try {
            JsonNode jsonNode1 = (JsonNode) jsonNode;
            return jsonNode1.get(key);
        } catch (Exception ex) {
            throw new RemoteCallException(ReqStatusEnum.PARSE_RESULT_ERROR, String.valueOf(jsonNode), key);
        }
    }

}

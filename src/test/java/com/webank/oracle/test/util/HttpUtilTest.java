package com.webank.oracle.test.util;

import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.webank.oracle.base.enums.ReqStatusEnum;
import com.webank.oracle.base.utils.HttpUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class HttpUtilTest {

    public static final String[] URL_ARRAY = new String[]{
            "https://api.kraken.com/0/public/Ticker?pair=ETHXBT",
            "https://api.exchangerate-api.com/v4/latest/CNY",
            "https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new"
    };

    private String request(String type, String url, Map<String, String> parameterMap, Object object) {
        try {
            switch (StringUtils.upperCase(type)) {
                case "GET":
                    return HttpUtil.get(url, parameterMap);
                case "POST":
                    return HttpUtil.post(url, parameterMap, object);
                default:
                    break;
            }
            return "";
        } catch (SocketTimeoutException e) {
            String errorMsg = ExceptionUtils.getRootCauseMessage(e);
            log.error("{}",errorMsg);
            log.error("Request:[{}], error:[{}]", url, ReqStatusEnum.getBySocketErrorMsg(errorMsg));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void testGet() {
        for (String url : URL_ARRAY) {
            String result = request("get", url, null, null);
            Assertions.assertTrue(StringUtils.isNotBlank(result));
        }
    }

    @Test
    public void testHttpUtilException() {
        // host unavailable
        request("get", "http://127.255.252.242/get", null, null);

        // read timeout
        request("get", "http://httpbin.org/delay/20", null, null);

        // 404
        request("get", "https://httpstat.us/404", null, null);
    }
}


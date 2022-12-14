package com.webank.truora.restcontroller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/*
* 实现两个本地数据源的桩方法
*
* rand： 多行结果，每一行是一个整形的随机数
* 访问其的url是：http://localhost:5022/truora/source/rand
* 或plain(http://localhost:5022/truora/source/rand)
* 对应： https://www.random.org/integers/?num=10&min=1&max=1000&col=1&base=10&format=plain&rnd=new
*
*
* exchange:  返回一个json，rates数组里包含汇率
* 访问其的url格式是 json(http://localhost:5022/truora/source/exchange).rates.RND
* 对应：  https://api.exchangerate-api.com/v4/latest/CNY，
*
* 之所以实现这两个伪数据源，主要用于测试验证，屏蔽可能存在的不可控的网络问题
* */
class Exchange {
    public String provider = "https://www.exchangerate-api.com";
    public String WARNING_UPGRADE_TO_V6 = "https://www.exchangerate-api.com/docs/free";
    public String terms = "https://www.exchangerate-api.com/terms";
    public String base = "CNY";
    public String date = "2022-12-13";
    public int time_last_updated = 1670889601;
    public Map<String, Double> rates = new HashMap();

}

@Api(value = "/source", tags = "source stub")
@Slf4j
@RestController
@RequestMapping(value = "/source", produces = {"application/JSON"})
public class SourceStub {
    @GetMapping("/rand")
    public String rand() {
        String res = "";
        for (int i = 0; i < 3; i++) {
            int r = (int) (Math.random() * 10000);
            res = res + String.valueOf(r) + "\n";
        }

        return res;
    }

    @GetMapping("/exchange")
    public Exchange exchange() {

        Exchange e = new Exchange();
        e.rates.put("CNY", Double.valueOf(1));
        e.rates.put("JPY", Double.valueOf(19.66));
        e.rates.put("USD", Double.valueOf(0.143));
        e.rates.put("RND", Double.valueOf(Math.random()*10));
        return e;
    }

}


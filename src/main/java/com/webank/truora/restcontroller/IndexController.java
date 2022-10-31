package com.webank.truora.restcontroller;

import com.webank.truora.base.config.ServerConfig;
import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

@Slf4j
@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired private ServerConfig serverConfig;

    @GetMapping(value = "version")
    public BaseResponse version() {
        log.info("Request version:[{}]", System.currentTimeMillis());
        return new BaseResponse(ConstantCode.SUCCESS, serverConfig.getVersion());
    }

    @GetMapping(value = "index")
    public BaseResponse index(HttpServletRequest request) {
        List<String> urls = new ArrayList<String>();
        //String servername = request.getLocalName();
        //int port = request.getLocalPort();
        //urls.add(String.format("http://%s:%s",servername,port));
        urls.add("/truora/index");
        urls.add("/truora/version");
        urls.add("/truora/chain/group/list");
        urls.add("/truora/oracle/address");
        urls.add("/truora/oracle/address?chainId=chain0&groupId=group0");

        urls.add("/truora/history/query/280ac496641910d37a6e6d4638f67844f1efdf00869838213c1e98ec94909337");
        urls.add("/truora/history/list?chainId=chain0&groupId=group0&pageSize=50&hideResult=false");


        //urls.add("/truora/center/list");
        return new BaseResponse(ConstantCode.SUCCESS, urls);
    }




}
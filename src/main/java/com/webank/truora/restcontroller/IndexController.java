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
//@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@RequestMapping("/")
public class IndexController {

    @Autowired private ServerConfig serverConfig;

    @GetMapping(value = "version")
    public BaseResponse version() {
        if (serverConfig ==null) {
            return new BaseResponse(ConstantCode.SUCCESS, "UNKNOWN");

        }
        log.info("Request version:[{}]", System.currentTimeMillis());
        return new BaseResponse(ConstantCode.SUCCESS, serverConfig.getVersion());
    }

    @GetMapping(value = "index")
    public BaseResponse index(HttpServletRequest request) {
        List<String> urls = new ArrayList<String>();
        String servername = request.getServerName();
        int port = request.getLocalPort();
        String serverRoot = String.format("http://%s:%s",servername,port);
        urls.add(serverRoot);
        urls.add(serverRoot+"/truora/index");
        urls.add(serverRoot+"/truora/version");
        urls.add(serverRoot+"/truora/chain/group/list");
        urls.add(serverRoot+"/truora/oracle/address");
        urls.add(serverRoot+"/truora/oracle/address?chainId=chain0&groupId=group0");

        urls.add(serverRoot+"/truora/history/query/280ac496641910d37a6e6d4638f67844f1efdf00869838213c1e98ec94909337");
        urls.add(serverRoot+"/truora/history/list?chainId=chain0&groupId=group0&pageSize=50&hideResult=false");
        urls.add(serverRoot+"/truora/dapps/get");
        urls.add(serverRoot+"/truora/dapps/get?url=1");
        urls.add(serverRoot+"/truora/dapps/get?url=2");
        urls.add(serverRoot+"/truora/dapps/get?url=3");
        urls.add(serverRoot+"/truora/dapps/get?url=4&input=sampletext");
        urls.add(serverRoot+"/truora/dapps/vrf/deploy?type=25519");
        urls.add(serverRoot+"/truora/dapps/vrf/get?type=25519");
        urls.add(serverRoot+"/truora/dapps/vrf/get?type=k1");
        urls.add(serverRoot+"/truora/source/exchange");
        urls.add(serverRoot+"/truora/source/rand");
        urls.add(serverRoot+"/truora/source/text?input=sampletext");
        //urls.add("/truora/center/list");
        return new BaseResponse(ConstantCode.SUCCESS, urls);
    }




}
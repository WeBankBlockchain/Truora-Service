/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos2runner.vrf.VRFService;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.database.DBReqHistoryRepository;
import com.webank.truora.database.DBReqHistoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * return encrypt type to web 0 is standard, 1 is guomi
 */
@Api(value = "/history", tags = "Query request history by request id")
@Slf4j
@RestController
@RequestMapping(value = "/history")
public class DBReqHistoryController {

    @Autowired
    private DBReqHistoryRepository reqHistoryRepository;
    @Autowired
    private DBReqHistoryService reqHistoryService;
    @Autowired
    private VRFService vrfService;

    @GetMapping("query/{requestId}")
    public BaseResponse query(@PathVariable("requestId") String requestId) {
        if (StringUtils.isBlank(requestId)) {
            return new BaseResponse(ConstantCode.PARAM_EXCEPTION);
        }
        Optional<DBReqHistory> reqHistory = reqHistoryRepository.findByReqId(requestId);
        if (reqHistory.isPresent()) {
            return new BaseResponse(ConstantCode.SUCCESS, reqHistory.get());
        }

        return new BaseResponse(ConstantCode.DATA_NOT_EXISTS, requestId);
    }

    @GetMapping("list")
    public BaseResponse list(
            @RequestParam(value = "chainId", defaultValue = "") String chainId,
            @RequestParam(value = "groupId", defaultValue = "") String groupId,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumberParam,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSizeParam,
            @RequestParam(value = "hideResult", defaultValue = "true") boolean hideResult
    ) {

        // 默认从第 1 页开始
        int pageNumber = pageNumberParam <= 0 ? 0 : pageNumberParam - 1;
        // 默认一页 100 条，不能超过 100 条每页
        int pageSize = pageSizeParam <= 0 || pageSizeParam > 100 ? 100 : pageSizeParam;

        Page<DBReqHistory> reqHistoryPage = reqHistoryService.getReqHistoryList(chainId, groupId, pageNumber, pageSize, hideResult);

        return BaseResponse.pageResponse(ConstantCode.SUCCESS, reqHistoryPage.getContent(), reqHistoryPage.getTotalElements());

    }

    @GetMapping("random/decode")
    public BaseResponse decodeProof(
            @RequestParam(value = "requestId", required = false) String requestId,
            @RequestParam(value = "proof", required = false) String proof,
            @RequestParam(value = "chainId", defaultValue = "1") String chainId,
            @RequestParam(value = "groupId", defaultValue = "1") String groupId) {
        if (StringUtils.isAllBlank(proof, requestId)) {
            return new BaseResponse(ConstantCode.PARAM_EXCEPTION);
        }

        String proofToDecode = proof;
        try {
            if (StringUtils.isNotBlank(requestId)) {
                Optional<DBReqHistory> reqHistory = reqHistoryRepository.findByReqId(requestId);
                if (reqHistory.isPresent() && StringUtils.isNotBlank(reqHistory.get().getProof())) {
                    proofToDecode = reqHistory.get().getProof();
                }
            }
            if (StringUtils.isBlank(proofToDecode)) {
                // no proof to decode
                return new BaseResponse(ConstantCode.PARAM_EXCEPTION);
            }

            Pair<String, String> decodeProof = vrfService.decodeProof(chainId, groupId, proofToDecode);
            if (StringUtils.equalsAnyIgnoreCase(decodeProof.getKey(), "0x0")) {
                return new BaseResponse(ConstantCode.SUCCESS, decodeProof.getValue());
            }
            return new BaseResponse(ConstantCode.DECODE_PROOF_ERROR, decodeProof.getKey());
        } catch (Exception e) {
            log.error("Decode proof error:[{}:{}:{}]", chainId, groupId, proofToDecode, e);
            return new BaseResponse(ConstantCode.DECODE_PROOF_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
    }
}

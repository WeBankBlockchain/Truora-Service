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

package com.webank.oracle.history;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.transaction.vrf.VRFService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * return encrypt type to web 0 is standard, 1 is guomi
 */
@Api(value = "/history", tags = "Query request history by request id")
@Slf4j
@RestController
@RequestMapping(value = "/history")
public class ReqHistoryController {

    @Autowired private ReqHistoryRepository reqHistoryRepository;
    @Autowired private VRFService vrfService;

    @GetMapping("query/{requestId}")
    public BaseResponse query(@PathVariable("requestId") String requestId) {
        if (StringUtils.isBlank(requestId)) {
            return new BaseResponse(ConstantCode.PARAM_EXCEPTION);
        }
        Optional<ReqHistory> reqHistory = reqHistoryRepository.findByReqId(requestId);
        if (reqHistory.isPresent()) {
            return new BaseResponse(ConstantCode.SUCCESS, reqHistory.get());
        }

        return new BaseResponse(ConstantCode.DATA_NOT_EXISTS, requestId);
    }

    @GetMapping("list")
    public BaseResponse list(
            @RequestParam(value = "chainId", defaultValue = "1") int chainId,
            @RequestParam(value = "groupId", defaultValue = "1") int groupId,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumberParam,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSizeParam,
            @RequestParam(value = "hideResult", defaultValue = "true") boolean hideResult
    ) {

        // 默认从第 1 页开始
        int pageNumber = pageNumberParam <= 0 ? 0 : pageNumberParam - 1;
        // 默认一页 10 条，不能超过 20 条每页
        int pageSize = pageSizeParam <= 0 || pageSizeParam > 20 ? 10 : pageSizeParam;

        // sort desc
        Sort.TypedSort<ReqHistory> sortType = Sort.sort(ReqHistory.class);
        Sort sort = sortType.by(ReqHistory::getModifyTime).descending();

        // page
        PageRequest page = PageRequest.of(pageNumber, pageSize, sort);

        long count = reqHistoryRepository.countByChainIdAndGroupId(chainId, groupId);
        if (count > 0) {
            Page<ReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdOrderByModifyTimeDesc(chainId, groupId, page);
            if (hideResult) {
                reqHistoryPage.getContent().forEach((history) -> {
                    history.setResult("");
                    history.setProof("");
                });
            }
            return BaseResponse.pageResponse(ConstantCode.SUCCESS, reqHistoryPage.getContent(), count);
        } else {
            return BaseResponse.emptyPageResponse(ConstantCode.SUCCESS);
        }
    }

    @GetMapping("random/decode")
    public BaseResponse decodeProof(
            @RequestParam(value = "requestId", required = false) String requestId,
            @RequestParam(value = "proof", required = false) String proof,
            @RequestParam(value = "chainId", defaultValue = "1") int chainId,
            @RequestParam(value = "groupId", defaultValue = "1") int groupId) {
        if (StringUtils.isAllBlank(proof, requestId)) {
            return new BaseResponse(ConstantCode.PARAM_EXCEPTION);
        }

        String proofToDecode = proof;
        try {
            if (StringUtils.isNotBlank(requestId)) {
                Optional<ReqHistory> reqHistory = reqHistoryRepository.findByReqId(requestId);
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

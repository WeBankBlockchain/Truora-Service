package com.webank.oracle.history;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */

@Service
public  class ReqHistoryService  {

    @Autowired
    private ReqHistoryRepository reqHistoryRepository;

   public BaseResponse getReqHistroyList(int chainId, int groupId, int pageNumber, int pageSize, boolean hideResult) {
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

   public ReqHistory getLatestRecord(int chainId, int groupId, int sourceType) {

       // sort desc
       Sort.TypedSort<ReqHistory> sortType = Sort.sort(ReqHistory.class);
       Sort sort = sortType.by(ReqHistory::getModifyTime).descending();

       // page
       PageRequest page = PageRequest.of(0, 1, sort);

       long count = reqHistoryRepository.countByChainIdAndGroupIdAndSourceType(chainId, groupId,sourceType);
       if (count > 0) {
           Page<ReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdAndSourceTypeOrderByModifyTimeDesc(chainId, groupId, sourceType,page);

           return reqHistoryPage.getContent().get(0);
       }
       return null;
   }
}
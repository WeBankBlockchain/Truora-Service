package com.webank.oracle.history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 */

@Service
public  class ReqHistoryService  {

    @Autowired
    private ReqHistoryRepository reqHistoryRepository;

   public  Page<ReqHistory> getReqHistoryList(int chainId, int groupId, int pageNumber, int pageSize, boolean hideResult) {
       // sort desc
       Sort.TypedSort<ReqHistory> sortType = Sort.sort(ReqHistory.class);
       Sort sort = sortType.by(ReqHistory::getModifyTime).descending();

       PageRequest page = PageRequest.of(pageNumber, pageSize, sort);

       Page<ReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdOrderByModifyTimeDesc(chainId, groupId, page);
       if (hideResult) {
           reqHistoryPage.getContent().forEach((history) -> {
               history.setResult("");
               history.setProof("");
           });
       }

        return reqHistoryPage;

   }

   public ReqHistory getLatestRecord(int chainId, int groupId, int sourceType) {

       Sort.TypedSort<ReqHistory> sortType = Sort.sort(ReqHistory.class);
       Sort sort = sortType.by(ReqHistory::getCreateTime).descending();

       // limit 0,1
       PageRequest page = PageRequest.of(0, 1, sort);
       Page<ReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdAndSourceTypeOrderByCreateTimeDesc(chainId, groupId, sourceType,page);
       List<ReqHistory> resultList = reqHistoryPage.getContent();
       return resultList.isEmpty() ? null : resultList.get(0);


   }
}
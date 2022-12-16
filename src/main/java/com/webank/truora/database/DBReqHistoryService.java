package com.webank.truora.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */

@Service
public  class DBReqHistoryService {

    @Autowired
    private DBReqHistoryRepository reqHistoryRepository;

   public  Page<DBReqHistory> getReqHistoryList(String chainId, String groupId, int pageNumber, int pageSize, boolean hideResult) {
       // sort desc
       Sort.TypedSort<DBReqHistory> sortType = Sort.sort(DBReqHistory.class);
       Sort sort = sortType.by(DBReqHistory::getModifyTime).descending();

       PageRequest page = PageRequest.of(pageNumber, pageSize, sort);

       Page<DBReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdOrderByModifyTimeDesc(chainId, groupId, page);
       if (hideResult) {
           reqHistoryPage.getContent().forEach((history) -> {
               history.setResult("");
               history.setProof("");
           });
       }

        return reqHistoryPage;

   }

   public DBReqHistory getLatestRecord(String chainId, String groupId, int sourceType) {

       Sort.TypedSort<DBReqHistory> sortType = Sort.sort(DBReqHistory.class);
       Sort sort = sortType.by(DBReqHistory::getCreateTime).descending();

       // limit 0,1
       PageRequest page = PageRequest.of(0, 1, sort);
       Page<DBReqHistory> reqHistoryPage = reqHistoryRepository.findByChainIdAndGroupIdAndSourceTypeOrderByCreateTimeDesc(chainId, groupId, sourceType,page);
       List<DBReqHistory> resultList = reqHistoryPage.getContent();
       return resultList.isEmpty() ? null : resultList.get(0);


   }
}
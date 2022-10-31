package com.webank.truora.test.repository;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.database.DBReqHistoryService;
import com.webank.truora.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 *
 */

@Slf4j
public class ReqHistoryRepositoryTest extends BaseTest {

    @Autowired private DBReqHistoryService reqHistoryService;

    public static final String REQ_ID = "reqId-" + System.currentTimeMillis();
    public static final String REQ_QUERY = "reqQuery-0000000000000001";
    public static final String USER_CONTRACT = "userContract-0xdafssafdsadf";
    public static final String USER_CONTRACT_NEW = USER_CONTRACT + "-new";


    @Test
    public void testSave() {
        DBReqHistory reqHistory = new DBReqHistory();
        reqHistory.setReqId(REQ_ID);
        reqHistory.setReqQuery(REQ_QUERY);
        reqHistory.setUserContract(USER_CONTRACT);

        DBReqHistory savedReqHistory = reqHistoryRepository.save(reqHistory);

        // check if get a new id
        Assertions.assertTrue(savedReqHistory.getId() > 0);

        // test findById
        Optional<DBReqHistory> reqHistoryExists = reqHistoryRepository.findById(savedReqHistory.getId());

        Assertions.assertTrue(reqHistoryExists.isPresent());
        log.info("old req history:[{}]", reqHistoryExists.get());
        Assertions.assertTrue(StringUtils.equals(REQ_ID, reqHistoryExists.get().getReqId()));
        Assertions.assertTrue(StringUtils.equals(REQ_QUERY, reqHistoryExists.get().getReqQuery()));
        Assertions.assertTrue(StringUtils.equals(USER_CONTRACT, reqHistoryExists.get().getUserContract()));


        // test update
        DBReqHistory reqHistoryInDB = reqHistoryExists.get();
        reqHistoryInDB.setUserContract(USER_CONTRACT_NEW);
        this.reqHistoryRepository.save(reqHistoryInDB);

        reqHistoryExists = reqHistoryRepository.findById(reqHistoryInDB.getId());

        Assertions.assertTrue(reqHistoryExists.isPresent());
        log.info("new req history:[{}]", reqHistoryExists.get());
        Assertions.assertTrue(StringUtils.equals(REQ_ID, reqHistoryExists.get().getReqId()));
        Assertions.assertTrue(StringUtils.equals(REQ_QUERY, reqHistoryExists.get().getReqQuery()));
        Assertions.assertTrue(StringUtils.equals(USER_CONTRACT_NEW, reqHistoryExists.get().getUserContract()));
    }

    @Test
    public void testFindLatestBlockNumber() {

        DBReqHistory savedReqHistory = reqHistoryService.getLatestRecord("1","1", SourceTypeEnum.URL.getId());

        // check if get a new id
        Assertions.assertTrue(savedReqHistory.getBlockNumber() .intValue()> 0);

        // test findById
    }

    @Test
    public void testGetReqHistroyList() {
        Page pageResult = reqHistoryService.getReqHistoryList("1","1",0, 3, true);
        long total = pageResult.getTotalElements();
        // check if get a new id
        log.info("********: " + total);
        Assertions.assertTrue(total> 0);

        Page pageResult1 = reqHistoryService.getReqHistoryList("1","1",0, 10, true);
        long total1 = pageResult1.getTotalElements();
        Assertions.assertEquals(total, total1);
        // test findById
    }


    @Test
    public void testGetLatestRecord() {
        DBReqHistory reqHistory = reqHistoryService.getLatestRecord("1","1",SourceTypeEnum.URL.getId());

        Assertions.assertTrue(reqHistory != null);
    }
}

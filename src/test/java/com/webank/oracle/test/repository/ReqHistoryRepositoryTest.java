package com.webank.oracle.test.repository;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.webank.oracle.history.ReqHistoryRepository;
import com.webank.oracle.history.ReqHistory;
import com.webank.oracle.test.base.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class ReqHistoryRepositoryTest extends BaseTest {

    @Autowired private ReqHistoryRepository reqHistoryRepository;

    public static final String REQ_ID = "reqId-" + System.currentTimeMillis();
    public static final String REQ_QUERY = "reqQuery-0000000000000001";
    public static final String USER_CONTRACT = "userContract-0xdafssafdsadf";
    public static final String USER_CONTRACT_NEW = USER_CONTRACT + "-new";


    @Test
    public void testSave() {
        ReqHistory reqHistory = new ReqHistory();
        reqHistory.setReqId(REQ_ID);
        reqHistory.setReqQuery(REQ_QUERY);
        reqHistory.setUserContract(USER_CONTRACT);

        ReqHistory savedReqHistory = reqHistoryRepository.save(reqHistory);

        // check if get a new id
        Assertions.assertTrue(savedReqHistory.getId() > 0);

        // test findById
        Optional<ReqHistory> reqHistoryExists = reqHistoryRepository.findById(savedReqHistory.getId());

        Assertions.assertTrue(reqHistoryExists.isPresent());
        log.info("old req history:[{}]", reqHistoryExists.get());
        Assertions.assertTrue(StringUtils.equals(REQ_ID, reqHistoryExists.get().getReqId()));
        Assertions.assertTrue(StringUtils.equals(REQ_QUERY, reqHistoryExists.get().getReqQuery()));
        Assertions.assertTrue(StringUtils.equals(USER_CONTRACT, reqHistoryExists.get().getUserContract()));


        // test update
        ReqHistory reqHistoryInDB = reqHistoryExists.get();
        reqHistoryInDB.setUserContract(USER_CONTRACT_NEW);
        this.reqHistoryRepository.save(reqHistoryInDB);

        reqHistoryExists = reqHistoryRepository.findById(reqHistoryInDB.getId());

        Assertions.assertTrue(reqHistoryExists.isPresent());
        log.info("new req history:[{}]", reqHistoryExists.get());
        Assertions.assertTrue(StringUtils.equals(REQ_ID, reqHistoryExists.get().getReqId()));
        Assertions.assertTrue(StringUtils.equals(REQ_QUERY, reqHistoryExists.get().getReqQuery()));
        Assertions.assertTrue(StringUtils.equals(USER_CONTRACT_NEW, reqHistoryExists.get().getUserContract()));
    }
}
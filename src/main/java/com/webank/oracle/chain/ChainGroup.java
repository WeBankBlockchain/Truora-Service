package com.webank.oracle.chain;

import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@Data
public class ChainGroup {

    private int chainId;

    private List<Integer> groupIdList;
}
package com.webank.truora.base.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 */

@Slf4j
@Data
public class ChainGroup {

    private String platform = "fiscobcos2";

    private String chainId;

    private List<String> groupIdList;
}
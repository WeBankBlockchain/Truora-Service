package com.webank.truora.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 *
 */


@Component
public class DBContractDeployService {

    @Autowired private DBContractDeployRepository contractDeployRepository;

    public Page<DBContractDeploy> selectContractDeployList(String platform,String chainId, String groupId, int pageNumber, int pageSize ) {
        // sort desc
        Sort.TypedSort<DBContractDeploy> sortType = Sort.sort(DBContractDeploy.class);
        Sort sort = sortType.by(DBContractDeploy::getCreateTime).descending();

        PageRequest page = PageRequest.of(pageNumber, pageSize, sort);

        if (chainId.isEmpty()){
            return  this.contractDeployRepository.findAll(page);
        }

        if (chainId.isEmpty()){
            return  this.contractDeployRepository.findByPlatformAndChainId(platform,chainId,page);
        }

        return  this.contractDeployRepository.findByPlatformAndChainIdAndGroupId(platform,chainId,groupId, page);
    }
}
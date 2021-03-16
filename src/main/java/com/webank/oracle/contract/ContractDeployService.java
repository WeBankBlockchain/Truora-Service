package com.webank.oracle.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 *
 */


@Component
public class ContractDeployService {

    @Autowired private ContractDeployRepository contractDeployRepository;

    public Page<ContractDeploy> selectContractDeployList(int chainId, int groupId, int pageNumber, int pageSize ) {
        // sort desc
        Sort.TypedSort<ContractDeploy> sortType = Sort.sort(ContractDeploy.class);
        Sort sort = sortType.by(ContractDeploy::getCreateTime).descending();

        PageRequest page = PageRequest.of(pageNumber, pageSize, sort);

        if (chainId <= 0){
            return  this.contractDeployRepository.findAll(page);
        }

        if (groupId <= 0){
            return  this.contractDeployRepository.findByChainId(chainId,page);
        }

        return  this.contractDeployRepository.findByChainIdAndGroupId(chainId,groupId, page);
    }
}
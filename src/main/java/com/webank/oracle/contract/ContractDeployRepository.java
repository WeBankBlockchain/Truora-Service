package com.webank.oracle.contract;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */


public interface ContractDeployRepository extends JpaRepository<ContractDeploy, Long> {

    /**
     *
     * @param chainId
     * @param groupId
     * @return
     */
    Optional<ContractDeploy> findByChainIdAndGroupIdAndContractType(int chainId, int groupId,int contractType);
}
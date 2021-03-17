package com.webank.oracle.contract;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Optional<ContractDeploy> findByChainIdAndGroupIdAndContractTypeAndVersion(int chainId, int groupId,int contractType, String version);


    List<ContractDeploy> findByEnable(boolean enable);

    Page<ContractDeploy> findAll(Pageable pageable);

    Page<ContractDeploy> findByChainId(int chainId, Pageable pageable);

    Page<ContractDeploy> findByChainIdAndGroupId(int chainId, int groupId, Pageable pageable);

}
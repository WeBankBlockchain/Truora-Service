package com.webank.truora.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 */


public interface DBContractDeployRepository extends JpaRepository<DBContractDeploy, Long> {


    /**
     *
     * @param chainId
     * @param groupId
     * @return
     */
    Optional<DBContractDeploy> findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(String platform, String chainId, String groupId, int contractType, String version);


    List<DBContractDeploy> findByEnable(boolean enable);

    Page<DBContractDeploy> findAll(Pageable pageable);

    Page<DBContractDeploy> findByPlatformAndChainId(String platform, String chainId, Pageable pageable);

    Page<DBContractDeploy> findByPlatformAndChainIdAndGroupId(String platform, String chainId, String groupId, Pageable pageable);

}
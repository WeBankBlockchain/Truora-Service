package com.webank.truora.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 */


public interface DBReqHistoryRepository extends JpaRepository<DBReqHistory, Long> {

    /**
     *  Find by req_id.
     *
     * @param reqId
     * @return
     */
    Optional<DBReqHistory> findByReqId(String reqId);

    //todo
//    @Query(value = "select r from ReqHistory r where r.chainId = ?1 and r.groupId = ?2 order by r.modifyTime DESC LIMIT 0,1" ,nativeQuery = true)
//    ReqHistory findLastestByChainIdAndGroupId(String chainId,String groupId);
    /**
     *
     * @return
     */
    long count();

    long countByChainIdAndGroupId(String chainId,String groupId);

    long countByChainIdAndGroupIdAndSourceType(String chainId,String groupId, int sourceType);

    Page<DBReqHistory> findByChainIdAndGroupIdOrderByModifyTimeDesc(String chainId, String groupId, Pageable pageable);

    Page<DBReqHistory> findByChainIdAndGroupIdAndSourceTypeOrderByCreateTimeDesc(String chainId, String groupId, int sourceType, Pageable pageable);

//    List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);
//
//    // Enables the distinct flag for the query
//    List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
//    List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);
//
//    // Enabling ignoring case for an individual property
//    List<Person> findByLastnameIgnoreCase(String lastname);
//    // Enabling ignoring case for all suitable properties
//    List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);
//
//    // Enabling static ORDER BY for a query
//    List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
//    List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
}
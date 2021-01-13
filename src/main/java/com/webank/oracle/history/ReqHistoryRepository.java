package com.webank.oracle.history;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */


public interface ReqHistoryRepository extends JpaRepository<ReqHistory, Long> {

    /**
     *  Find by req_id.
     *
     * @param reqId
     * @return
     */
    Optional<ReqHistory> findByReqId(String reqId);

    //todo
//    @Query(value = "select r from ReqHistory r where r.chainId = ?1 and r.groupId = ?2 order by r.modifyTime DESC LIMIT 0,1" ,nativeQuery = true)
//    ReqHistory findLastestByChainIdAndGroupId(int chainId,int groupId);
    /**
     *
     * @return
     */
    long count();

    long countByChainIdAndGroupId(int chainId,int groupId);

    long countByChainIdAndGroupIdAndSourceType(int chainId,int groupId, int sourceType);

    Page<ReqHistory> findByChainIdAndGroupIdOrderByModifyTimeDesc(int chainId,int groupId,Pageable pageable);

    Page<ReqHistory> findByChainIdAndGroupIdAndSourceTypeOrderByCreateTimeDesc(int chainId, int groupId, int sourceType, Pageable pageable);

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
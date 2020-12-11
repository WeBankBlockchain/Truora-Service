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

    /**
     *
     * @return
     */
    long count();

    long countByChainIdAndGroupId(int chainId,int groupId);

    Page<ReqHistory> findByChainIdAndGroupIdOrderByModifyTimeDesc(int chainId,int groupId,Pageable pageable);

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
package com.orkblan.FlashCash.repositories;

import com.orkblan.FlashCash.domain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;



public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    @Query(value ="SELECT a.iban FROM Account a WHERE a.user_id = :id", nativeQuery = true)
    String findByUser(Long id);

    @Query(value = "SELECT * FROM Account a WHERE a.user_id = :id", nativeQuery = true)
    Account findAccountByUserid(Long id);
}

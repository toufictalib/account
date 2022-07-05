package com.example.account.dao;

import java.util.List;

import com.example.account.dao.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "SELECT * FROM  ACCOUNT_TRANSACTION WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
	List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);
	
}

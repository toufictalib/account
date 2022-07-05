package com.example.account.service;

import java.util.List;

import com.example.account.dao.model.Transaction;
import com.example.account.service.model.TransactionDto;

public interface ITransactionService {

	void save(Transaction transaction);

	List<TransactionDto> findTransactions(Long accountId);

}

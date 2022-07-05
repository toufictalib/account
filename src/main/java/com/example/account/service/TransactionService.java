package com.example.account.service;

import java.util.List;

import com.example.account.dao.TransactionRepository;
import com.example.account.dao.model.Transaction;
import com.example.account.service.model.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionService implements ITransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Override
	public void save(Transaction transaction) {

		transactionRepository.save(transaction);

		log.info("Transaction [{}] was created successfully", transaction);
	}

	@Override
	public List<TransactionDto> findTransactions(Long accountId) {

		return transactionRepository.findTransactionsByAccountId(accountId).stream()
			.map(e -> TransactionDto.builder()
				.id(e.getId())
				.amount(e.getAmount())
				.description(e.getDescription())
				.creationTime(e.getCreationTime())
				.build())
			.toList();
	}
}

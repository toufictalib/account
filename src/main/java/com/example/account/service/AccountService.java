package com.example.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.account.dao.AccountRepository;
import com.example.account.dao.CustomerRepository;
import com.example.account.dao.model.Account;
import com.example.account.dao.model.Customer;
import com.example.account.dao.model.Transaction;
import com.example.account.service.exception.NotFoundException;
import com.example.account.service.model.AccountDto;
import com.example.account.service.model.AccountRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService implements IAccountService {

	private final ITransactionService transactionService;
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;

	@Override
	@Transactional
	public AccountDto openAccount(Long customerId, AccountRequest accountRequest) {

		log.info("Opening an account for customer [{}] with [{}]", customerId, accountRequest);
		if (!isCustomerExist(customerId)) {
			log.info("Customer is not existing [{}]", customerId);
			throw new NotFoundException("Customer is not existing ::" + customerId);
		}

		var creationTime = LocalDateTime.now();
		var modificationTime = LocalDateTime.now();

		// create account
		var account = Account.builder()
			.description(accountRequest.getDescription())
			.balance(BigDecimal.valueOf(accountRequest.getInitialCredit()))
			.customer(Customer.builder().id(customerId).build())
			.creationTime(creationTime)
			.modificationTime(modificationTime)
			.build();

		var createdAccount = accountRepository.save(account);

		if (accountRequest.getInitialCredit() > 0) {
			var transaction = Transaction.builder()
				.description("Open Account Initial Credit")
				.amount(BigDecimal.valueOf(accountRequest.getInitialCredit()))
				.account(createdAccount)
				.creationTime(creationTime)
				.modificationTime(modificationTime)
				.build();

			transactionService.save(transaction);
		}

		var accountDto = AccountDto.builder()
			.id(createdAccount.getId())
			.description(createdAccount.getDescription())
			.build();

		log.info("Account [{}] was created successfully", createdAccount);
		return accountDto;
	}

	private boolean isCustomerExist(Long customerId) {
		return customerRepository.findById(customerId).isPresent();
	}
}

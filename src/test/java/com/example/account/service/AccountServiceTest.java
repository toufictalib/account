package com.example.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.account.dao.AccountRepository;
import com.example.account.dao.CustomerRepository;
import com.example.account.dao.model.Account;
import com.example.account.dao.model.Customer;
import com.example.account.dao.model.Transaction;
import com.example.account.service.exception.NotFoundException;
import com.example.account.service.model.AccountDto;
import com.example.account.service.model.AccountRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class AccountServiceTest {

	@Mock
	private ITransactionService transactionService;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private AccountService accountService;

	@Test
	void testOpenAccount_customerNotExisting() {

		// given
		var accountId = 2L;
		var accountRequest = AccountRequest.builder()
			.description("Open Account")
			.initialCredit(10)
			.build();

		when(customerRepository.findById(accountId)).thenReturn(Optional.empty());

		// when
		var thrown = catchThrowable(() -> accountService.openAccount(accountId, accountRequest));

		// then
		assertThat(thrown).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testOpenAccount_initialCreditIsZero_noTransaction() {

		// given
		var customerId = 1L;
		var accountId = 2L;
		var initialCredit = 0;
		var accountRequest = AccountRequest.builder()
			.description("Open Account")
			.initialCredit(initialCredit)
			.build();

		var accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
		var account = Account.builder()
			.description(accountRequest.getDescription())
			.balance(BigDecimal.valueOf(initialCredit))
			.customer(Customer.builder().id(customerId).build())
			.build();

		var accountResponse = Account.builder()
			.id(accountId)
			.description(accountRequest.getDescription())
			.build();

		when(customerRepository.findById(accountId)).thenReturn(Optional.of(Customer.builder().build()));
		when(accountRepository.save(accountArgumentCaptor.capture())).thenReturn(accountResponse);

		// when
		var result = accountService.openAccount(accountId, accountRequest);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(AccountDto.builder()
			.id(accountResponse.getId())
			.description(accountResponse.getDescription())
			.build());
		assertThat(accountArgumentCaptor).usingRecursiveComparison().ignoringExpectedNullFields()
			.isEqualTo(account);
		verify(transactionService, never()).findTransactions(any());
	}

	@Test
	void testOpenAccount_initialCreditIsBiggerThanZero_hasTransaction() {

		// given
		var customerId = 1L;
		var accountId = 2L;
		var initialCredit = 10;
		var accountRequest = AccountRequest.builder()
			.description("Open Account")
			.initialCredit(initialCredit)
			.build();

		var accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
		var transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);

		var account = Account.builder()
			.description(accountRequest.getDescription())
			.balance(BigDecimal.valueOf(initialCredit))
			.customer(Customer.builder().id(customerId).build())
			.build();

		var accountResponse = Account.builder()
			.id(accountId)
			.balance(account.getBalance())
			.description(accountRequest.getDescription())
			.customer(account.getCustomer())
			.build();

		when(customerRepository.findById(accountId)).thenReturn(Optional.of(Customer.builder().build()));
		when(accountRepository.save(accountArgumentCaptor.capture())).thenReturn(accountResponse);
		doNothing().when(transactionService).save(transactionArgumentCaptor.capture());

		// when
		var result = accountService.openAccount(accountId, accountRequest);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(AccountDto.builder()
			.id(accountResponse.getId())
			.description(accountResponse.getDescription())
			.build());

		assertThat(accountArgumentCaptor).usingRecursiveComparison().ignoringExpectedNullFields()
			.isEqualTo(account);
		
		assertThat(transactionArgumentCaptor.getValue()).usingRecursiveComparison().ignoringExpectedNullFields()
			.isEqualTo(Transaction.builder()
				.description("Open Account Initial Credit")
				.amount(BigDecimal.valueOf(initialCredit))
				.account(account)
				.build());
	}
}

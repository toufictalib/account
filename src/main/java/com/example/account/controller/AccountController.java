package com.example.account.controller;

import java.util.List;

import com.example.account.service.IAccountService;
import com.example.account.service.ITransactionService;
import com.example.account.service.model.AccountDto;
import com.example.account.service.model.AccountRequest;
import com.example.account.service.model.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final IAccountService accountService;
	private final ITransactionService transactionService;

	@PostMapping("/{customerId}")
	public AccountDto openAccount(@PathVariable Long customerId, @RequestBody AccountRequest accountRequest) {
		return accountService.openAccount(customerId, accountRequest);
	}

	@GetMapping("/transactions/{accountId}")
	public List<TransactionDto> findTransactions(@PathVariable Long accountId) {
		return transactionService.findTransactions(accountId);
	}
}

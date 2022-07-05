package com.example.account.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import com.example.account.service.IAccountService;
import com.example.account.service.ITransactionService;
import com.example.account.service.model.AccountDto;
import com.example.account.service.model.AccountRequest;
import com.example.account.service.model.TransactionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(properties = "application.environment=DEV", controllers = AccountController.class)
class AccountControllerTest {

	@MockBean
	private IAccountService accountService;

	@MockBean
	private ITransactionService transactionService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testOpenAccount() throws Exception {

		// given
		var customerId = 1L;
		var accountId = 2L;
		var accountRequest = AccountRequest.builder()
			.description("Open Account")
			.initialCredit(10)
			.build();

		var expectedAccount = AccountDto.builder()
			.id(accountId)
			.description(accountRequest.getDescription())
			.build();

		when(accountService.openAccount(eq(customerId), refEq(accountRequest))).thenReturn(expectedAccount);

		// when
		var result = mockMvc.perform(post("/account/{customerId}", customerId)
			.content(objectMapper.writeValueAsString(accountRequest))
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		assertThat(result.andReturn().getResponse().getContentAsString()).isEqualTo(
			objectMapper.writeValueAsString(expectedAccount));
	}

	@Test
	void testFindTransactions() throws Exception {

		// given
		var accountId = 2L;
		var expectedTransactions = List.of(TransactionDto.builder()
			.id(1L)
			.description("test 1")
			.amount(BigDecimal.valueOf(10L))
			.build());

		when(transactionService.findTransactions(accountId)).thenReturn(expectedTransactions);

		// when
		var result = mockMvc.perform(get("/account/transactions/{accountId}", accountId)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		assertThat(result.andReturn().getResponse().getContentAsString()).isEqualTo(
			objectMapper.writeValueAsString(expectedTransactions));
	}
}

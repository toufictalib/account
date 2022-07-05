package com.example.account.service;

import com.example.account.service.model.AccountDto;
import com.example.account.service.model.AccountRequest;

public interface IAccountService {

	AccountDto openAccount(Long customerId, AccountRequest accountRequest);

}

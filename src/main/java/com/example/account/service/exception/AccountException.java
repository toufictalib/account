package com.example.account.service.exception;

public class AccountException extends RuntimeException {

	private String code;

	public AccountException(String code, String message) {
		super(message);
		this.code = code;
	}

	public AccountException(String code, String message, RuntimeException exception) {
		super(message, exception);
		this.code = code;
	}

}

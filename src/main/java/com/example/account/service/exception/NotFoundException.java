package com.example.account.service.exception;

public class NotFoundException extends AccountException {

	public NotFoundException(String message) {
		this(message, null);
	}

	public NotFoundException(String message, RuntimeException exception) {
		super("1001", message, exception);
	}
}

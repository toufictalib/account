package com.example.account.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@ToString
public class AccountRequest {

	private String description;

	@Builder.Default
	private Integer initialCredit = 0;

}

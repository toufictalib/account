package com.example.account.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AccountDto {

	private Long id;
	private String description;

}

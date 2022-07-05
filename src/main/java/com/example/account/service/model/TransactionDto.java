package com.example.account.service.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TransactionDto {

	private Long id;
	private String description;
	private BigDecimal amount;
	private LocalDateTime creationTime;

}

package com.example.account.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.example.account.dao.model.Account;
import com.example.account.dao.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryTest {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private AccountRepository accountRepository;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(accountRepository).isNotNull();
	}

	@Test
	void testSaveAccount() {

		// given
		var account = Account.builder()
			.description("test transaction")
			.balance(BigDecimal.valueOf(10L))
			.customer(Customer.builder()
				.id(1L)
				.build())
			.build();

		// when
		var result = accountRepository.save(account);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(account);
	}
}

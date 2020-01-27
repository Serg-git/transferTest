package ru.ert.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.ert.account.exception.InsufficientBalanceException;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.dto.TransactionResult;
import ru.ert.account.model.dto.TransactionStatus;
import ru.ert.account.model.dto.TransferTransaction;
import ru.ert.account.service.impl.AccountingServiceImpl;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AccountingServiceImpl acountingService;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}


	@Test
	public void testCreateAccount() {
		Account createtdAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", new Account(BigDecimal.valueOf(100)), Account.class);
		assertNotNull(createtdAccount);
		assertNotEquals(createtdAccount.getId(), 0);
		System.out.println(createtdAccount);
	}

	@Test
	public void testGetAllAccounts() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/accounts/all",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetAccountById() {
		Account createtdAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", new Account(BigDecimal.valueOf(100)), Account.class);
		Account account = restTemplate.getForObject(getRootUrl() + "/accounts/"+Long.toString(createtdAccount.getId()), Account.class);
		assertNotNull(account);
		assertEquals(account.getId(), createtdAccount.getId());
		System.out.println(account);
	}

	@Test
	public void testNotFoundAccountById() {
		ResponseEntity<Account> postResponse = restTemplate.getForEntity(getRootUrl() + "/accounts/1000", Account.class);
		assertNotNull(postResponse);
		assertEquals(postResponse.getStatusCode(), HttpStatus.NOT_FOUND);
		System.out.println(postResponse);
	}



	@Test
	public void testTransfer() {
		Account account = new Account(BigDecimal.valueOf(100));
		Account firstAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(firstAccount);

		account = new Account(BigDecimal.valueOf(200));
		Account secondAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(secondAccount);

		RestTemplate restTemplate = new RestTemplate();

		// Low money
		TransferTransaction transaction = new TransferTransaction(Long.valueOf(firstAccount.getId()), Long.valueOf(secondAccount.getId()), BigDecimal.valueOf(1000));
		TransactionResult result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.FAIL.getCode());
		System.out.println(result.toString());

		// No account
		transaction = new TransferTransaction(1000L, Long.valueOf(secondAccount.getId()), BigDecimal.valueOf(10));
		result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.FAIL.getCode());
		System.out.println(result.toString());

		// OK
		transaction = new TransferTransaction(Long.valueOf(firstAccount.getId()), Long.valueOf(secondAccount.getId()), BigDecimal.valueOf(10));
		result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.SUCCESS.getCode());
		System.out.println(result.toString());
	}

	@Test
	public void testInsufficientBalanceException() throws InsufficientBalanceException {
		Account firstAccount  = new Account(BigDecimal.valueOf(100));
		Account secondAccount = new Account(BigDecimal.valueOf(200));

		// Low money
		Throwable thrown = catchThrowable(() -> {
			acountingService.transferAmount(firstAccount, secondAccount, BigDecimal.valueOf(1000));
		});
		assertThat(thrown).isInstanceOf(InsufficientBalanceException.class);
		assertThat(thrown.getMessage()).isNotBlank();
	}

	@Test
	public void testResourceNotFoundException() throws ResourceNotFoundException {
		// No account
		Throwable thrown = catchThrowable(() -> {
			acountingService.retrieveAccountById(1000l);
		});
		assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
		assertThat(thrown.getMessage()).isNotBlank();
	}
}

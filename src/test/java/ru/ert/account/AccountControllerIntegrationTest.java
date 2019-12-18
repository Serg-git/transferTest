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
import ru.ert.account.model.Account;
import ru.ert.account.model.TransactionResult;
import ru.ert.account.model.TransactionStatus;
import ru.ert.account.model.TransferTransaction;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
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
		Account account = restTemplate.getForObject(getRootUrl() + "/accounts/1", Account.class);
		System.out.println(account);
		assertNotNull(account);
	}

	@Test
	public void testNotFoundAccountById() {
		Account account = restTemplate.getForObject(getRootUrl() + "/accounts/1000", Account.class);
		assertNotNull(account);
		assertEquals(account.getId(), 0);
		System.out.println(account);
	}

	@Test
	public void testCreateAccount() {
		Account account = new Account(0,BigDecimal.valueOf(100));
		ResponseEntity<Account> postResponse = restTemplate.postForEntity(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testTransfer() {
		Account account = new Account(1L,BigDecimal.valueOf(100));
		Account createtdAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(createtdAccount);

		account = new Account(2L,BigDecimal.valueOf(200));
		createtdAccount = restTemplate.postForObject(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(createtdAccount);

		RestTemplate restTemplate = new RestTemplate();

		// Low money
		TransferTransaction transaction = new TransferTransaction(1L, 2L, BigDecimal.valueOf(1000));
		TransactionResult result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.FAIL.getCode());
		System.out.println(result.toString());

		// No account
		transaction = new TransferTransaction(1000L, 2L, BigDecimal.valueOf(10));
		result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.FAIL.getCode());
		System.out.println(result.toString());

		// OK
		transaction = new TransferTransaction(1L, 2L, BigDecimal.valueOf(10));
		result = restTemplate.postForObject(getRootUrl() + "/accounts/transfer", transaction, TransactionResult.class);
		assertEquals(result.getCode(), TransactionStatus.SUCCESS.getCode());
		System.out.println(result.toString());
	}
}

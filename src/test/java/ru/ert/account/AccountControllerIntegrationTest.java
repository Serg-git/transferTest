package ru.ert.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import ru.ert.account.model.Account;
import ru.ert.account.repository.AccountsRepository;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	//@Autowired
	//private AccountsRepository accountsRepository;

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
	public void testCreateAccount() {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(100));

		ResponseEntity<Account> postResponse = restTemplate.postForEntity(getRootUrl() + "/accounts/new", account, Account.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}


}

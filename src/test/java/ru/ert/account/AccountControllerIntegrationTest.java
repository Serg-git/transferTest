package ru.ert.account;

import org.junit.Before;
import org.junit.BeforeClass;
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

	@Autowired
	private AccountsRepository accountsRepository;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		//return "http://localhost:8080";
		return "http://localhost:" + port;
	}

	@Before
	public void setup() throws Exception {
		initClient();
	}

	@Test
	public void contextLoads() {

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
	private void initClient() throws Exception {
		Account account = new Account(0, BigDecimal.valueOf(100));
		accountsRepository.save(account);
		account = new Account(0, BigDecimal.valueOf(200));
		accountsRepository.save(account);
	}
/*
	@Test
	public void testUpdateAccount() {
		int id = 1;
		Account account = restTemplate.getForObject(getRootUrl() + "/accounts/" + id, Account.class);
		account.setBalance(500);

		restTemplate.put(getRootUrl() + "/accounts/" + id, account);

		Account updatedEmployee = restTemplate.getForObject(getRootUrl() + "/accounts/" + id, Account.class);
		assertNotNull(updatedEmployee);
	}

	@Test
	public void testDeleteAccount() {
		int id = 2;
		Account account = restTemplate.getForObject(getRootUrl() + "/accounts/" + id, Account.class);
		assertNotNull(account);

		restTemplate.delete(getRootUrl() + "/accounts/" + id);

		try {
			account = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Account.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
*/
}

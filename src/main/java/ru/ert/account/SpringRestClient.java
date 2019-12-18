package ru.ert.account;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ert.account.model.Account;
import ru.ert.account.model.TransactionResult;
import ru.ert.account.model.TransferTransaction;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Client Application
 *
 * @author kuyantsev
 * Date: 06.12.2019
 */
public class SpringRestClient {

    private static final String GET_ACCOUNTS_ENDPOINT_URL = "http://localhost:8080/accounts/all";
    private static final String GET_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/{id}";
    private static final String CREATE_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/new";
    private static final String TRANSFER_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/transfer";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringRestClient springRestClient = new SpringRestClient();

        // Step 1: first create a new account
        springRestClient.createAccount( BigDecimal.valueOf(100));
        springRestClient.createAccount( BigDecimal.valueOf(200));
        springRestClient.createAccount( BigDecimal.valueOf(300));

        // Step 2: get new created account from step1
        springRestClient.getAccountById(1L);

        // Step 3: get not exist account
        springRestClient.getAccountById(105L);

        // Step 4: get all accounts
        springRestClient.getAccounts();

        // Step 5: Try transfer from 1 to 2 accounts (low balance)
        springRestClient.transfer(1L, 2L, BigDecimal.valueOf(510));
        springRestClient.getAccounts();

        // Step 6: Try transfer from 100 to 2 accounts (not found account)
        springRestClient.transfer(100L, 2L, BigDecimal.valueOf(10));
        springRestClient.getAccounts();

        // Step 7: Try transfer from 1 to 2 accounts (ok)
        springRestClient.transfer(1L,2L, BigDecimal.valueOf(10));
        springRestClient.getAccounts();

    }

    private void getAccounts() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> result = restTemplate.exchange(GET_ACCOUNTS_ENDPOINT_URL, HttpMethod.GET, entity, String.class);

        System.out.println(result);
    }

    private void getAccountById(Long id) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id.toString());

        String result;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(GET_ACCOUNT_ENDPOINT_URL, HttpMethod.GET, entity, String.class, params);
            result = response.getBody();
        } catch (HttpStatusCodeException e) {
            result = e.getResponseBodyAsString();
        }
        catch (RestClientException e) {
            result = e.getMessage();
        }

        System.out.println(result);
    }

    private void createAccount(BigDecimal balance) {
        Account newAccount = new Account(0, balance);
        Account result = restTemplate.postForObject(CREATE_ACCOUNT_ENDPOINT_URL, newAccount, Account.class);
        System.out.println(result);
    }


    private void transfer(long sourceId, Long targetId, BigDecimal amount) {
        TransferTransaction transaction = new TransferTransaction(sourceId, targetId, amount);
        RestTemplate restTemplate = new RestTemplate();
        TransactionResult result = restTemplate.postForObject(TRANSFER_ACCOUNT_ENDPOINT_URL, transaction, TransactionResult.class);
        System.out.println(result.toString());
    }

}

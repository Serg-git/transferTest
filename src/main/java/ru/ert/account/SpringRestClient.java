package ru.ert.account;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ert.account.model.Account;
import ru.ert.account.model.TransactionResult;
import ru.ert.account.model.TransactionStatus;
import ru.ert.account.model.TransferTransaction;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpringRestClient {

    private static final String GET_ACCOUNTS_ENDPOINT_URL = "http://localhost:8080/accounts/all";
    private static final String GET_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/{id}";
    private static final String CREATE_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/new";
    private static final String UPDATE_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/{id}";
    private static final String DELETE_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/{id}";
    private static final String TRANSFER_ACCOUNT_ENDPOINT_URL = "http://localhost:8080/accounts/transfer";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringRestClient springRestClient = new SpringRestClient();

        // Step1: first create a new employee

        springRestClient.createAccount(BigDecimal.valueOf(100));
        springRestClient.createAccount(BigDecimal.valueOf(200));
        springRestClient.createAccount(BigDecimal.valueOf(300));

        // Step 2: get new created account from step1
        springRestClient.getAccountById(Long.valueOf(1));

        // Step 3: get new created account from step1
        springRestClient.getAccountById(Long.valueOf(105));

        // Step 4: get all accounts
        springRestClient.getAccounts();

        // Step 5: Update account with id = 1
        //springRestClient.updateAccount(Long.valueOf(1), BigDecimal.valueOf(1300));

        // Step 6: Delete account with id = 3
        //springRestClient.deleteAccount(Long.valueOf(3);

        // Step 7: Transfer from 1 to 2 accounts
        springRestClient.transfer(Long.valueOf(1), Long.valueOf(2), BigDecimal.valueOf(510));
        springRestClient.getAccounts();

        springRestClient.transfer(Long.valueOf(100), Long.valueOf(2), BigDecimal.valueOf(10));
        springRestClient.getAccounts();

        springRestClient.transfer(Long.valueOf(1), Long.valueOf(2), BigDecimal.valueOf(10));
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

        RestTemplate restTemplate = new RestTemplate();
        //Account result = restTemplate.getForObject(GET_ACCOUNT_ENDPOINT_URL, Account.class, params);

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
        Account newAccount = new Account(balance);
        RestTemplate restTemplate = new RestTemplate();
        Account result = restTemplate.postForObject(CREATE_ACCOUNT_ENDPOINT_URL, newAccount, Account.class);
        System.out.println(result);
    }


    private void updateAccount(Long id, BigDecimal balance) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id.toString());

        RestTemplate restTemplate = new RestTemplate();

        Account updatedAccount = restTemplate.getForObject(GET_ACCOUNT_ENDPOINT_URL, Account.class, params);
        updatedAccount.setBalance(balance);

        restTemplate.put(UPDATE_ACCOUNT_ENDPOINT_URL, updatedAccount, params);
    }

    private void deleteAccount(Long id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id.toString());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(DELETE_ACCOUNT_ENDPOINT_URL, params);
    }


    private void transfer(long sourceId, Long targetId, BigDecimal amount) {
        //TransferTransaction transaction = new TransferTransaction(Long.valueOf(1), Long.valueOf(2), BigDecimal.valueOf(510));
        TransferTransaction transaction = new TransferTransaction(sourceId, targetId, amount);
        RestTemplate restTemplate = new RestTemplate();
        TransactionResult result = restTemplate.postForObject(TRANSFER_ACCOUNT_ENDPOINT_URL, transaction, TransactionResult.class);
        System.out.println(result.toString());
    }

}

package ru.ert.account.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.TransferTransaction;
import ru.ert.account.service.AccountingService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Request handler
 * @author kuyantsev
 * Date: 06.12.2019
 */

@RestController
@RequestMapping("accounts")
public class Controller {
    /** Bank accounting service */

    @Autowired
    private AccountingService service;

    /**
     * Returns ResponseEntity with list of accounts
     * @return {ResponseEntity} response with list of accounts
     */
    @GetMapping("/all")
    public ResponseEntity handleRetrieveAllAccountsRequest() {
        try {
            return ResponseEntity.ok(service.retrieveAllAccounts());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Returns ResponseEntity with requested account details
     * @param accountId requested account ID
     * @return {ResponseEntity} response with requested account details
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> handleRetrieveAccountByIdRequest(@PathVariable("accountId") Long accountId) throws ResourceNotFoundException  {
/*
        Optional<Account> account = service.retrieveAccountById(accountId);
        return account.isPresent() ? ResponseEntity.ok().body(account.get()) :
                                     ResponseEntity.ok().body("Account not found for this id : " + accountId);
*/
        Account account = service.retrieveAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + accountId));
        return ResponseEntity.ok().body(account);
    }

    /**
     * Create new account
     * @param account
     * @return {ResponseEntity} response with requested account details
     */
    @PostMapping("/new")
    public ResponseEntity createAccount(@Valid @RequestBody Account account) {
        try {
            return ResponseEntity.ok(service.createAccount(account));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Handle
     * @return {ResponseEntity} response with requested account details
     */

    @PostMapping("/transfer")
    public ResponseEntity handleTransferTransactionRequest(@Valid @RequestBody TransferTransaction transaction) {
        try {
            return ResponseEntity.ok(service.transfer(transaction));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}

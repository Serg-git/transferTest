package ru.ert.account.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.dto.TransferTransaction;
import ru.ert.account.service.AccountingService;

import javax.validation.Valid;

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
     * All accounts
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
     * Get account by ID
     * @param accountId requested account ID
     * @return {ResponseEntity} response with requested account details
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> handleRetrieveAccountByIdRequest(@PathVariable("accountId") Long accountId) throws ResourceNotFoundException  {
        return ResponseEntity.ok().body(service.retrieveAccountById(accountId));
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
     * Transfer from account
     * param in transaction
     * @return {ResponseEntity} response with transfer status
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

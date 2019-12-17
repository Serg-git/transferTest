package ru.ert.account.service;

import ru.ert.account.exception.InsufficientBalanceException;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.TransactionResult;
import ru.ert.account.model.TransferTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for using bank accounting
 * @author kuyantsev
 * Date: 06.12.2019
 */
public interface AccountingService {

    /**
     * Retrieve all accounts
     * @return {@link List} accounts
     * @throws Exception in case of a retrieve operation error
     */
    List<Account> retrieveAllAccounts() throws Exception ;

    /**
     * Retrieve account by its ID
     * @param id account ID
     * @return {@link Account} account
     * @throws Exception in case of a retrieve operation error
     */
    Account retrieveAccountById(Long id) throws ResourceNotFoundException;

    /**
     * Create new account
     * @param account
     * @return {@link Account} account
     * @throws Exception in case of a create operation error
     */
    Account createAccount(Account account) throws Exception ;

    /**
    * Transfer money from one account to another
    * @param transaction transfer request details
    * @return {@link TransactionResult} transaction result details
    * @throws Exception in case of an error during transfer transaction
    */
    TransactionResult transfer(TransferTransaction transaction) throws Exception ;

}

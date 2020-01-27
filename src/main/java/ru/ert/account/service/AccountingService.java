package ru.ert.account.service;

import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.dto.TransactionResult;
import ru.ert.account.model.dto.TransferTransaction;

import java.util.List;

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
     * Save account
     * @param account
     * @return {@link Account} account
     * @throws Exception in case of a create operation error
     */
    Account saveAccount(Account account) throws Exception ;

    /**
    * Transfer money from one account to another
    * @param transaction transfer request details
    * @return {@link TransactionResult} transaction result details
    * @throws Exception in case of an error during transfer transaction
    */
    TransactionResult transfer(TransferTransaction transaction) throws Exception ;

}

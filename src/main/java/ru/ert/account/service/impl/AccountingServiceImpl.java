package ru.ert.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ert.account.exception.InsufficientBalanceException;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.dto.TransactionResult;
import ru.ert.account.model.dto.TransactionStatus;
import ru.ert.account.model.dto.TransferTransaction;
import ru.ert.account.repository.AccountsRepository;
import ru.ert.account.service.AccountingService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Bank accounting service implementation
 *
 * @author kuyantsev
 * Date: 06.12.2019
 */
@Service
public class AccountingServiceImpl implements AccountingService {
    private static final String LOW_BALANS_MESSAGE = "Not enough money to complete transfer operation. " +
            "Current balance: %s, Requested amount: %s";
    private static final String ACCOUNT_NOT_FOUND = "Account not found for this id :: ";

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public List<Account> retrieveAllAccounts() throws Exception {
        return accountsRepository.findAll();
    }

    @Override
    public Account retrieveAccountById(Long id) throws ResourceNotFoundException {
        return accountsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND + id));
    }

    @Override
    public Account createAccount(Account account) throws Exception {
        return accountsRepository.save(account);
    }

    @Override
    @Transactional
    public TransactionResult transfer(TransferTransaction transaction) {
        try {
            Account source = retrieveAccountById(transaction.getSourceId());
            Account target = retrieveAccountById(transaction.getTargetId());
            transferAmount(source, target, transaction.getAmount());
            return new TransactionResult(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            return new TransactionResult(TransactionStatus.FAIL, e.getMessage());
        }
    }

    public void transferAmount(Account source, Account target, BigDecimal amount) throws Exception  {
        withdraw(source, amount);
        deposit(target, amount);
        List<Account> accountList = Arrays.asList(source, target);
        accountsRepository.saveAll(accountList);
    }

    private void withdraw(Account account, BigDecimal amount) throws InsufficientBalanceException {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    private void deposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }
}

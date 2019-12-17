package ru.ert.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import ru.ert.account.exception.InsufficientBalanceException;
import ru.ert.account.exception.InsufficientBalanceException;
import ru.ert.account.exception.ResourceNotFoundException;
import ru.ert.account.model.Account;
import ru.ert.account.model.TransactionResult;
//import ru.ert.account.model.TransactionStatus;
import ru.ert.account.model.TransactionStatus;
import ru.ert.account.model.TransferTransaction;
import ru.ert.account.repository.AccountsRepository;
import ru.ert.account.service.AccountingService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public List<Account> retrieveAllAccounts() {
        return accountsRepository.findAll();
    }

    @Override
    public Optional<Account> retrieveAccountById(Long id) {
        return accountsRepository.findById(id);
    }

    @Override
    public Account createAccount(Account account) {
        return accountsRepository.save(account);
    }

    @Override
    @Transactional
    public TransactionResult transfer(TransferTransaction transaction) throws Exception {
        try {
            Account source = retrieveAccountById(transaction.getSourceId()).orElseThrow(() -> new ResourceNotFoundException("Source account not found for this id :: " + transaction.getSourceId()));
            Account target = retrieveAccountById(transaction.getTargetId()).orElseThrow(() -> new ResourceNotFoundException("Target account not found for this id :: " + transaction.getTargetId()));
            transfer(source, target, transaction.getAmount());
            return new TransactionResult(TransactionStatus.SUCCESS);
        } catch (InsufficientBalanceException e) {
            return new TransactionResult(TransactionStatus.FAIL, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new TransactionResult(TransactionStatus.FAIL, e.getMessage());
        }

    }

    private void transfer(Account source, Account target, BigDecimal amount) throws InsufficientBalanceException  {
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

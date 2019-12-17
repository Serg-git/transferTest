package ru.ert.account.exception;

import java.math.BigDecimal;

/**
 * Exception in case there are not enough money
 * @author kuyantsev
 * Date: 06.12.2019
 */

public class InsufficientBalanceException extends Exception {
    private static final String MESSAGE = "Not enough money to complete transfer operation. " +
            "Current balance: %s, Requested amount: %s";

    public InsufficientBalanceException(BigDecimal balance, BigDecimal requestedAmount) {
        super(String.format(MESSAGE, balance, requestedAmount));
    }
}

package ru.ert.account.model;

/**
 * Transaction statuses
 * @author kuyantsev
 * Date: 06.12.2019
 */
public enum TransactionStatus {

    SUCCESS (1),

    FAIL (2);

    private int code;

    TransactionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

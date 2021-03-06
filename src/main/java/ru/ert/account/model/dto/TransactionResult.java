package ru.ert.account.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Transaction result details
 * @author kuyantsev
 *  Date: 06.12.2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResult {

    @JsonProperty("status")
    private String statusName;

    private int code;

    private String message;

    /**
     * Creates transaction result details
     * @param status  transaction status
     * @param message additional result description
     */
    public TransactionResult(TransactionStatus status, String message) {
        this(status.name(), status.getCode(), message);
    }

    /**
     * Creates transaction result details without message
     * @param status transaction status
     */
    public TransactionResult(TransactionStatus status) {
        this(status, null);
    }

    public TransactionResult(String message) {
        this(message.isEmpty() ? TransactionStatus.SUCCESS : TransactionStatus.FAIL, message);
    }

    @JsonCreator
    private TransactionResult(@JsonProperty("status") String status,
                              @JsonProperty("code") int code,
                              @JsonProperty("message") String message) {
        this.statusName = status;
        this.code = code;
        this.message = message;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", status=" + statusName + ", message=" + message +"]";
    }
}

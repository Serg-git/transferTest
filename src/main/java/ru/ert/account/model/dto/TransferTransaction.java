package ru.ert.account.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Transfer request details
 * @author kuyantsev
 * Date: 06.12.2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferTransaction {

    private Long sourceId;

    private Long targetId;

    private BigDecimal amount;

    @JsonCreator
    public TransferTransaction(@JsonProperty("sourceId") Long sourceId,
                               @JsonProperty("targetId") Long targetId,
                               @JsonProperty("amount") BigDecimal amount) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

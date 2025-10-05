package com.demo1.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private Currency currency;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime timestamp;
    private Account source_account_id;
    private Account target_account_id;

    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT, TRANSFER_EXTERNAL, FEE
    }

    public enum TransactionStatus {
        PENDING, SETTLED, FAILED
    }

    public enum Currency {
        MAD, USD, EUR
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Account getSource_account_id() {
        return source_account_id;
    }

    public void setSource_account_id(Account source_account_id) {
        this.source_account_id = source_account_id;
    }

    public Account getTarget_account_id() {
        return target_account_id;
    }

    public void setTarget_account_id(Account target_account_id) {
        this.target_account_id = target_account_id;
    }
}


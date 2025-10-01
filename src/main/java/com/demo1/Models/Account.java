package com.demo1.Models;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private AccountType type;
    private UUID clientId;
    private boolean status;

    public enum AccountType {
        CURRENT, SAVINGS, CREDIT
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClient(UUID clientId) {
        this.clientId = clientId;
    }
}


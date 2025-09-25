package com.demo1.Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Credit {

    private UUID id;
    private BigDecimal amount;
    private Currency currency;
    private double interestRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private CreditType creditType;
    private CreditStatus status;
    private Account account;

    public enum CreditType {
        SIMPLE, COMPOSED
    }

    public enum CreditStatus {
        PENDING, ACTIVE, LATE, CLOSED
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

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}


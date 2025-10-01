package com.demo1.Services;

import com.demo1.Models.Account;

import java.math.BigDecimal;

public interface TransactionService {

    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
}

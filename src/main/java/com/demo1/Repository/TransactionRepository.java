package com.demo1.Repository;

import com.demo1.Models.Account;

import java.math.BigDecimal;

public interface TransactionRepository {

    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
}

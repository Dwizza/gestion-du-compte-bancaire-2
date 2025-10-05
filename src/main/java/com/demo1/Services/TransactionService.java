package com.demo1.Services;

import com.demo1.Models.Account;
import com.demo1.Models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void transferInternal(Account from, Account to, BigDecimal amount);
    void transferOut(Account from, Account to, BigDecimal amount);
    List<Transaction> findAll();
}

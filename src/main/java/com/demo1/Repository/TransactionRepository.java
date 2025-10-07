package com.demo1.Repository;

import com.demo1.Models.Account;
import com.demo1.Models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository {
    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount, BigDecimal fee);
    void transferInternal(Account from, Account to, BigDecimal amount);
    void transferOut(Account from, Account to, BigDecimal amount, BigDecimal fee);
    List<Transaction> findAll();
}

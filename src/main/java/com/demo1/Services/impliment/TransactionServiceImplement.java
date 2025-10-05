package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.Account;
import com.demo1.Models.Transaction;
import com.demo1.Repository.TransactionRepository;
import com.demo1.Services.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TransactionServiceImplement implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImplement(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void deposit(Account account, BigDecimal amount){
        if (account == null || account.getId() == null) {
            throw new IllegalArgumentException("Account is required");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be strictly positive");
        }
        transactionRepository.deposit(account, amount);
    }

    @Override
    public void withdraw(Account account, BigDecimal amount) {
        if (account == null || account.getId() == null) {
            throw new IllegalArgumentException("Account is required");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be strictly positive");
        }
        try {
            transactionRepository.withdraw(account, amount);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("insufficient funds")) {
                throw new BusinessRuleViolationException("Insufficient funds.");
            }
            throw e;
        }
    }

    @Override
    public void transferInternal(Account from, Account to, BigDecimal amount) {
        if (from == null || from.getId() == null) {
            throw new IllegalArgumentException("Source account is required");
        }
        if (to == null || to.getId() == null) {
            throw new IllegalArgumentException("Destination account is required");
        }
        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        try {
            transactionRepository.transferInternal(from, to, amount);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("insufficient funds")) {
                throw new BusinessRuleViolationException("Insufficient funds.");
            }
            throw e;
        }
    }

    @Override
    public void transferOut(Account from, Account to, BigDecimal amount) {
        if (from == null || from.getId() == null) throw new IllegalArgumentException("Source account is required");
        if (to == null || to.getId() == null) throw new IllegalArgumentException("Destination account is required");
        if (from.getId().equals(to.getId())) throw new IllegalArgumentException("Accounts must be different");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be strictly positive");
        try {
            transactionRepository.transferOut(from, to, amount);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("insufficient funds")) {
                throw new BusinessRuleViolationException("Insufficient funds.");
            }
            throw e;
        }
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}

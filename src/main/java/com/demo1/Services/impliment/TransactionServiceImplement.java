package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.Account;
import com.demo1.Repository.TransactionRepository;
import com.demo1.Repository.implement.TransactionRepositoryImplement;
import com.demo1.Services.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionServiceImplement implements TransactionService {

    private static final TransactionRepository transactionRepository = new TransactionRepositoryImplement();

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
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("insufficient funds")) {
                throw new BusinessRuleViolationException("Insufficient funds.");
            }
            throw ex;
        }
    }
}

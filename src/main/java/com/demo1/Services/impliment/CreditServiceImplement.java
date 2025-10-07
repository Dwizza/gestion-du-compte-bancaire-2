package com.demo1.Services.impliment;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Models.Credit;
import com.demo1.Repository.CreditRepository;
import com.demo1.Services.CreditService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CreditServiceImplement implements CreditService {

    private final CreditRepository creditRepository;
    private BigDecimal lastMonthly = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public CreditServiceImplement(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public Credit createCredit(Client client, Account account, BigDecimal amount, Credit.CreditType type, int months) {
        if (client == null || client.getId() == null) throw new IllegalArgumentException("Client required");
        if (account == null || account.getId() == null) throw new IllegalArgumentException("Account required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (months <= 0) throw new IllegalArgumentException("Months must be >= 1");

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        double fixedRate = 5.0; // Intérêt fixe global (non annuel) demandé

        // Intérêt global (5% du principal) quel que soit le nombre de mois
        BigDecimal interest = amount.multiply(BigDecimal.valueOf(fixedRate)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalRepay = amount.add(interest).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthly = totalRepay.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        lastMonthly = monthly;

        // Seuils: <=50% OK (insertion PENDING), >50% rejet simple, >80% rejet fort
        if (client.getSalary() != null) {
            BigDecimal salary = client.getSalary().setScale(2, RoundingMode.HALF_UP);
            BigDecimal fifty = salary.multiply(BigDecimal.valueOf(0.50)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal eighty = salary.multiply(BigDecimal.valueOf(0.80)).setScale(2, RoundingMode.HALF_UP);

            if (monthly.compareTo(eighty) > 0) {
                throw new IllegalArgumentException("Monthly payment exceeds 80% of salary: credit impossible.");
            }
            if (monthly.compareTo(fifty) > 0) {
                throw new IllegalArgumentException("Monthly payment exceeds 50% of salary for chosen duration. Adjust amount or months.");
            }
        }

        Credit credit = new Credit();
        credit.setAmount(amount);
        credit.setCurrency(Credit.Currency.MAD);
        credit.setInterestRate(fixedRate); // stock du pourcentage global appliqué
        credit.setCreditType(Credit.CreditType.SIMPLE);
        credit.setStatus(Credit.CreditStatus.PENDING); // en attente d'approbation manager
        credit.setStartDate(LocalDate.now());
        credit.setEndDate(LocalDate.now().plusMonths(months));
        credit.setAccount(account);

        return creditRepository.insertCredit(credit, account);
    }

    @Override
    public Credit approveCredit(UUID creditId){
        return creditRepository.approveCredit(creditId);
    }

    @Override
    public Credit rejectCredit(UUID creditId){
        return creditRepository.rejectCredit(creditId);
    }

    @Override
    public List<Credit> listPending() {
        return creditRepository.findPending();
    }

    @Override
    public BigDecimal getLastMonthlyPayment() { return lastMonthly; }
}

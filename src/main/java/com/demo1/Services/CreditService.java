package com.demo1.Services;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Models.Credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CreditService {

    Credit createCredit(Client client, Account account, BigDecimal amount, Credit.CreditType type, int months);
    BigDecimal getLastMonthlyPayment();
    Credit approveCredit(UUID creditId);
        Credit rejectCredit(UUID creditId);
    List<Credit> listPending();
}

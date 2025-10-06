package com.demo1.Repository;

import com.demo1.Models.Account;
import com.demo1.Models.Credit;

import java.util.List;
import java.util.UUID;

public interface CreditRepository {
    Credit insertCredit(Credit credit, Account account);
    Credit findById(UUID id);
    List<Credit> findByClient(UUID clientId);
    List<Credit> findAll();
    Credit approveCredit(UUID creditId); // change status to ACTIVE + update account balance
    List<Credit> findPending();
}

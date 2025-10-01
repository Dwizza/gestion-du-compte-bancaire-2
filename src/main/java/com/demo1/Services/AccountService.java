package com.demo1.Services;

import com.demo1.Models.Account;
import com.demo1.Models.Client;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    void saveAccount(Client client, BigDecimal balance, Account.AccountType type);
    Account AccountByClient(Client client);
    List<Account> findByClient(Client client);
}

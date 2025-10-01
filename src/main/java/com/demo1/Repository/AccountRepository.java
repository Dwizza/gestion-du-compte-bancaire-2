package com.demo1.Repository;

import com.demo1.Models.Account;
import com.demo1.Models.Client;

import java.util.List;
import java.util.UUID;

public interface AccountRepository {

    void saveAccount(Account account, Client client);
    Account AccountByClient(Client client);
    List<Account> findByClientId(UUID clientId);
}

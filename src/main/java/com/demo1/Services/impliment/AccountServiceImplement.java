package com.demo1.Services.impliment;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Repository.AccountRepository;
import com.demo1.Services.AccountService;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public class AccountServiceImplement implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImplement(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void saveAccount(Client client, BigDecimal balance, Account.AccountType type){

        String prefix = "BK";
        int year = Year.now().getValue();
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Account account = new Account();

        account.setId(UUID.randomUUID());
        account.setAccountNumber(String.format("%s-%d-%s", prefix, year, uuidPart));
        account.setBalance(balance);
        account.setType(type);
        account.setClient(client.getId());
        account.setStatus(true);

        accountRepository.saveAccount(account, client);
    }

    public Account AccountByClient(Client client){


        return null;
    }

    @Override
    public List<Account> findByClient(Client client) {
        return accountRepository.findByClientId(client.getId());
    }
}

package com.demo1.Services;

import com.demo1.Models.Client;

import java.util.List;

public interface ClientService {

    Client save(String fullName, String address, String email, Double salary, Client.Currency currency, String cin);

    Client findByEmail(String email);
    Client findByCin(String cin);

    void editClient(String fullName, String address, String email, Double salary, Client.Currency currency, String cin, String recentEmail);

    void deleteClient(Client client);

    List<Client> findAll();
}

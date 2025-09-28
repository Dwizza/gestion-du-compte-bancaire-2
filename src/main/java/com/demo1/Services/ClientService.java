package com.demo1.Services;

import com.demo1.Models.Client;

import java.util.List;

public interface ClientService {

    Client save(String fullName, String address, String email, Double salary, Client.Currency currency);

    Client findByEmail(String email);

    void editClient(String fullName, String address, String email, Double salary, Client.Currency currency, String recentEmail);

    void deleteClient(Client client);

    List<Client> findAll();
}

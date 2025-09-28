package com.demo1.Repository;

import com.demo1.Models.Client;

import java.util.List;

public interface ClientRepository {

    void saveClient(Client client);
    Client findByEmail(String email);
    void updateClient(Client client, String recentEmail);
    void deleteClient(Client client);
    List<Client> findAll();
}

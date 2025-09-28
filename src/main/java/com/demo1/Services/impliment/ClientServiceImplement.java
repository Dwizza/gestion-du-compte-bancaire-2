package com.demo1.Services.impliment;

import com.demo1.Models.Client;
import com.demo1.Repository.ClientRepository;
import com.demo1.Repository.implement.ClientRepositoryImplement;
import com.demo1.Services.ClientService;

import java.math.BigDecimal;
import java.util.UUID;

public class ClientServiceImplement implements ClientService {

    private static ClientRepository clientRepository = new ClientRepositoryImplement();

    public Client save(String fullName, String address, String email, Double salary, Client.Currency currency){

        Client client = new Client();

        client.setId(UUID.randomUUID());
        client.setFullName(fullName);
        client.setAddress(address);
        client.setEmail(email);
        client.setSalary(BigDecimal.valueOf(salary));
        client.setCurrency(currency);

        clientRepository.saveClient(client);

        return client;
    }
}

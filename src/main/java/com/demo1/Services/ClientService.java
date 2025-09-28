package com.demo1.Services;

import com.demo1.Models.Client;

public interface ClientService {

    Client save(String fullName, String address, String email, Double salary, Client.Currency currency);

}

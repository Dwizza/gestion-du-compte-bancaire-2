package com.demo1.Controllers;

import com.demo1.Models.Client;
import com.demo1.Services.ClientService;
import com.demo1.Services.impliment.ClientServiceImplement;

import java.math.BigDecimal;
import java.util.Scanner;

public class ClientController {

    private static Scanner scanner = new Scanner(System.in);
    private static ClientService clientService = new ClientServiceImplement();

    public static void saveClient(){

        System.out.println("===========Create Client==========");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("address: ");
        String address = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Salary: ");
        Double salary = scanner.nextDouble();

        clientService.save(fullName, address, email, salary, Client.Currency.MAD);

    }
}

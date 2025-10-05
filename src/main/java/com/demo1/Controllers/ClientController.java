package com.demo1.Controllers;

import com.demo1.Models.Client;
import com.demo1.Services.ClientService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class ClientController {
    private static final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public void saveClient(){
        System.out.println("=========== Create Client ==========");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Salary: ");
        String salaryInput = scanner.nextLine().trim();
        System.out.print("CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        try {
            BigDecimal salary = new BigDecimal(salaryInput).setScale(2, RoundingMode.HALF_UP);
            Client client = clientService.save(fullName, address, email, salary.doubleValue(), Client.Currency.MAD, cin);
            System.out.println("Client created successfully with Email: " + client.getEmail());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void editClient(){
        System.out.println("=========== Edit Client ==========");
        System.out.print("Current Email: ");
        String recentEmail = scanner.nextLine().trim();
        Client existing = clientService.findByEmail(recentEmail);
        if (existing == null) {
            System.out.println("Client not found.");
            return;
        }
        System.out.print("New Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("New Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("New Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("New Salary: ");
        String salaryInput = scanner.nextLine().trim();
        System.out.print("New CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        try {
            BigDecimal salary = new BigDecimal(salaryInput).setScale(2, RoundingMode.HALF_UP);
            clientService.editClient(fullName, address, email, salary.doubleValue(), existing.getCurrency(), cin, recentEmail);
            System.out.println("Client updated successfully!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteClient(){
        System.out.println("=========== Delete Client ==========");
        System.out.print("Email of client to delete: ");
        String email = scanner.nextLine().trim();
        Client existing = clientService.findByEmail(email);
        if (existing == null) {
            System.out.println("Client not found.");
            return;
        }
        System.out.print("Are you sure you want to delete this client? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if ("y".equalsIgnoreCase(confirm)) {
            clientService.deleteClient(existing);
            System.out.println("Client deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public void listClients(){
        System.out.println("=========== Clients ==========");
        List<Client> clients = clientService.findAll();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            for (Client c : clients) {
                System.out.println("Name: " + c.getFullName() + ", Email: " + c.getEmail() + ", CIN: " + c.getCin() + ", Salary: " + c.getSalary());
            }
        }
    }
}

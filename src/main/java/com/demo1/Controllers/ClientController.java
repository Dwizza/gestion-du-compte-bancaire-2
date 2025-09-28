package com.demo1.Controllers;

import com.demo1.Models.Client;
import com.demo1.Services.ClientService;
import com.demo1.Services.impliment.ClientServiceImplement;
import com.demo1.Exceptions.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientController {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientService clientService = new ClientServiceImplement();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static void saveClient(){
        System.out.println("=========== Create Client ==========\n");
        try {
            String fullName = promptNonEmpty("Full Name: ");
            String address = promptNonEmpty("Address: ");
            String email = promptEmail("Email: ");
            BigDecimal salary = promptPositiveAmount("Salary: ");

            Client client = clientService.save(fullName, address, email, salary.setScale(2, RoundingMode.HALF_UP).doubleValue(), Client.Currency.MAD);
            System.out.println("Client created successfully with Email: " + client.getEmail());
        } catch (BusinessRuleViolationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void editClient(){
        System.out.println("=========== Edit Client ==========\n");
        String recentEmail = promptNonEmpty("Current Email: ");
        Client existing = clientService.findByEmail(recentEmail);
        if (existing == null) {
            System.out.println("Client not found.");
            return;
        }
        String fullName = promptNonEmpty("New Full Name: ");
        String address = promptNonEmpty("New Address: ");
        String email = promptEmail("New Email: ");
        BigDecimal salary = promptPositiveAmount("New Salary: ");
        clientService.editClient(fullName, address, email, salary.setScale(2, RoundingMode.HALF_UP).doubleValue(), existing.getCurrency(), recentEmail);
        System.out.println("Client updated successfully!");
    }

    public static void deleteClient(){
        System.out.println("=========== Delete Client ==========\n");
        String email = promptEmail("Email of client to delete: ");
        Client existing = clientService.findByEmail(email);
        if (existing == null) {
            System.out.println("Client not found.");
            return;
        }
        if (confirm("Are you sure you want to delete this client?")) {
            clientService.deleteClient(existing);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public static void listClients(){
        System.out.println("=========== Clients ==========\n");
        List<Client> clients = clientService.findAll();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            clients.forEach(System.out::println);
        }
    }

    private static boolean confirm(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String in = scanner.nextLine();
            if ("y".equalsIgnoreCase(in)) return true;
            if ("n".equalsIgnoreCase(in)) return false;
            System.out.println("Please answer with 'y' or 'n'.");
        }
    }

    private static String promptNonEmpty(String label) {
        while (true) {
            System.out.print(label);
            String v = scanner.nextLine();
            if (v != null) {
                v = v.trim();
            }
            if (v != null && !v.isEmpty()) {
                return v;
            }
            System.out.println("Value is required. Please try again.");
        }
    }

    private static String promptEmail(String label) {
        while (true) {
            String e = promptNonEmpty(label);
            if (EMAIL_PATTERN.matcher(e).matches()) {
                return e;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }

    private static BigDecimal promptPositiveAmount(String label) {
        while (true) {
            System.out.print(label);
            String input = scanner.nextLine();
            try {
                BigDecimal amount = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount;
                }
                System.out.println("Amount must be strictly positive.");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Please enter a numeric value (e.g., 1234.56).");
            }
        }
    }
}

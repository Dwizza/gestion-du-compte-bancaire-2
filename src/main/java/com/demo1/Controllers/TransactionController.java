package com.demo1.Controllers;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;
import com.demo1.Services.TransactionService;
import com.demo1.Services.impliment.AccountServiceImplement;
import com.demo1.Services.impliment.ClientServiceImplement;
import com.demo1.Services.impliment.TransactionServiceImplement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class TransactionController {
    private static Scanner scanner = new Scanner(System.in);
    private static ClientService clientService = new ClientServiceImplement();
    private static AccountService accountService = new AccountServiceImplement();
    private static TransactionService transactionService = new TransactionServiceImplement();


    public static void Deposit(){
        System.out.println("=========== Deposit ==========\n");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) {
            System.out.println("CIN is required.");
            return;
        }
        Client client = clientService.findByCin(cin);
        if (client == null) {
            System.out.println("Client not found for CIN: " + cin);
            return;
        }
        List<Account> accounts = accountService.findByClient(client);
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
            return;
        }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s%n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency() != null ? a.getCurrency().name() : "MAD");
        }
        int choice = promptChoice(1, accounts.size());
        Account selected = accounts.get(choice - 1);

        BigDecimal amount = promptPositiveAmount("Amount to deposit: ");
        try {
            transactionService.deposit(selected, amount.setScale(2, RoundingMode.HALF_UP));
            System.out.println("Deposit successful.");
        } catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public static void Withdraw(){
        System.out.println("=========== Withdraw ==========");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) {
            System.out.println("CIN is required.");
            return;
        }
        Client client = clientService.findByCin(cin);
        if (client == null) {
            System.out.println("Client not found for CIN: " + cin);
            return;
        }
        List<Account> accounts = accountService.findByClient(client);
        if (accounts == null || accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
            return;
        }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s%n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency() != null ? a.getCurrency().name() : "MAD");
        }
        int choice = promptChoice(1, accounts.size());
        Account selected = accounts.get(choice - 1);

        BigDecimal amount = promptPositiveAmount("Amount to withdraw: ");
        try {
            transactionService.withdraw(selected, amount.setScale(2, RoundingMode.HALF_UP));
            System.out.println("Withdraw successful.");
        } catch (Exception e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }

    private static int promptChoice(int min, int max) {
        while (true) {
            System.out.print("Enter choice [" + min + "-" + max + "]: ");
            String input = scanner.nextLine();
            try {
                int c = Integer.parseInt(input);
                if (c >= min && c <= max) return c;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Try again.");
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

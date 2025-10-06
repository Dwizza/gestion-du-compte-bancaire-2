package com.demo1.Controllers;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Models.Transaction;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;
import com.demo1.Services.TransactionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TransactionController {
    private static final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(ClientService clientService, AccountService accountService, TransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void Deposit() {
        System.out.println("=========== Deposit ==========");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) { System.out.println("CIN is required."); return; }
        Client client = clientService.findByCin(cin);
        if (client == null) { System.out.println("Client not found for CIN: " + cin); return; }
        List<Account> accounts = accountService.findByClient(client);
        if (accounts == null || accounts.isEmpty()) { System.out.println("No accounts found for this client."); return; }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s\n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency() != null ? a.getCurrency().name() : "MAD");
        }
        int choice;
        while (true) {
            System.out.print("Enter choice [1-" + accounts.size() + "]: ");
            String input = scanner.nextLine();
            try { choice = Integer.parseInt(input); if (choice >= 1 && choice <= accounts.size()) break; } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Try again.");
        }
        Account selected = accounts.get(choice - 1);
        BigDecimal amount;
        while (true) {
            System.out.print("Amount to deposit: ");
            String input = scanner.nextLine();
            try { amount = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP); if (amount.compareTo(BigDecimal.ZERO) > 0) break; System.out.println("Amount must be strictly positive."); }
            catch (NumberFormatException ex) { System.out.println("Invalid number. Please enter a numeric value (e.g., 1234.56)."); }
        }
        try { transactionService.deposit(selected, amount); System.out.println("Deposit successful."); }
        catch (Exception e) { System.out.println("Deposit failed: " + e.getMessage()); }
    }

    public void Withdraw() {
        System.out.println("=========== Withdraw ==========");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) {
            System.out.println("CIN is required.");
            return;
        }
        Client client = clientService.findByCin(cin);
        if (client == null) { System.out.println("Client not found for CIN: " + cin); return; }
        List<Account> accounts = accountService.findByClient(client);
        if (accounts == null || accounts.isEmpty()) { System.out.println("No accounts found for this client."); return; }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s\n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency() != null ? a.getCurrency().name() : "MAD");
        }
        int choice;
        while (true) {
            System.out.print("Enter choice [1-" + accounts.size() + "]: ");
            String input = scanner.nextLine();
            try { choice = Integer.parseInt(input); if (choice >= 1 && choice <= accounts.size()) break; } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Try again.");
        }
        Account selected = accounts.get(choice - 1);
        BigDecimal amount;
        while (true) {
            System.out.print("Amount to withdraw: ");
            String input = scanner.nextLine();
            try { amount = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP); if (amount.compareTo(BigDecimal.ZERO) > 0) break; System.out.println("Amount must be strictly positive."); }
            catch (NumberFormatException ex) { System.out.println("Invalid number. Please enter a numeric value (e.g., 1234.56)."); }
        }
        try { transactionService.withdraw(selected, amount); System.out.println("Withdraw successful."); }
        catch (Exception e) { System.out.println("Withdraw failed: " + e.getMessage()); }
    }

    public void TransferInternal() {
        System.out.println("=========== Internal Transfer (Own Accounts) ==========");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) { System.out.println("CIN is required."); return; }
        Client client = clientService.findByCin(cin);
        if (client == null) { System.out.println("Client not found."); return; }
        List<Account> accounts = accountService.findByClient(client);
        System.out.println("Accounts found for this client: " + (accounts == null ? 0 : accounts.size()));
        if (accounts != null) {
            for (Account a : accounts) {
                System.out.printf(" - %s | %s | Balance: %s %s\n", a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency());
            }
        }
        if (accounts == null || accounts.size() < 2) { System.out.println("Client needs at least 2 accounts to transfer internally."); return; }
        System.out.println("Select source account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account a = accounts.get(i);
            System.out.printf("%d) %s | %s | %s %s\n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency());
        }
        int src;
        while (true) {
            System.out.print("Source [1-" + accounts.size() + "]: ");
            try {
                src = Integer.parseInt(scanner.nextLine());
                if (src >= 1 && src <= accounts.size()) break;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid.");
        }
        Account from = accounts.get(src - 1);
        System.out.println("Select destination account:");
        int dst;
        while (true) {
            System.out.print("Destination [1-" + accounts.size() + "]: ");
            try { dst = Integer.parseInt(scanner.nextLine()); if (dst >= 1 && dst <= accounts.size() && dst != src) break; } catch (NumberFormatException ignored) {}
            System.out.println("Invalid (must be different from source).");
        }
        Account to = accounts.get(dst - 1);
        BigDecimal amount;
        while (true) {
            System.out.print("Amount to transfer: ");
            try { amount = new BigDecimal(scanner.nextLine()).setScale(2, RoundingMode.HALF_UP); if (amount.compareTo(BigDecimal.ZERO) > 0) break; } catch (Exception ignored) {}
            System.out.println("Invalid amount.");
        }
        try { transactionService.transferInternal(from, to, amount); System.out.println("Internal transfer done."); }
        catch (Exception e) { System.out.println("Transfer failed: " + e.getMessage()); }
    }

    public void TransferOut() {
        System.out.println("=========== Transfer Out (To Another Client) ==========");
        System.out.print("Enter source client CIN: ");
        String sourceCin = scanner.nextLine().trim().toUpperCase();
        if (sourceCin.isEmpty()) { System.out.println("CIN is required."); return; }
        Client sourceClient = clientService.findByCin(sourceCin);
        if (sourceClient == null) { System.out.println("Source client not found."); return; }
        List<Account> sourceAccounts = accountService.findByClient(sourceClient);
        if (sourceAccounts == null || sourceAccounts.isEmpty()) { System.out.println("No accounts for source client."); return; }
        sourceAccounts = sourceAccounts.stream().filter(a -> a.getType() == Account.AccountType.CURRENT).toList();
        if (sourceAccounts.isEmpty()) { System.out.println("Source client has no CURRENT account for transfer out."); return; }
        System.out.println("Select source CURRENT account:");
        for (int i = 0; i < sourceAccounts.size(); i++) {
            Account a = sourceAccounts.get(i);
            System.out.printf("%d) %s | Balance: %s %s\n", i + 1, a.getAccountNumber(), a.getBalance(), a.getCurrency());
        }
        int sIdx;
        while (true) {
            System.out.print("Choice [1-" + sourceAccounts.size() + "]: ");
            try {
                sIdx = Integer.parseInt(scanner.nextLine());
                if (sIdx >= 1 && sIdx <= sourceAccounts.size()) break; } catch (Exception ignored) {}
            System.out.println("Invalid.");
        }
        Account from = sourceAccounts.get(sIdx - 1);
        System.out.print("Enter destination client CIN: ");
        String destCin = scanner.nextLine().trim().toUpperCase();
        if (destCin.isEmpty()) {
            System.out.println("Destination CIN required.");
            return;
        }
        Client destClient = clientService.findByCin(destCin);
        if (destClient == null) {
            System.out.println("Destination client not found.");
            return;
        }
        if (destClient.getId().equals(sourceClient.getId())) {
            System.out.println("Use Internal Transfer for same client.");
            return;
        }
        List<Account> destAccounts = accountService.findByClient(destClient);
        if (destAccounts == null || destAccounts.isEmpty()) {
            System.out.println("Destination client has no accounts.");
            return;
        }
        System.out.println("Select destination account:");
        for (int i = 0; i < destAccounts.size(); i++) {
            Account a = destAccounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s\n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency());
        }
        int dIdx;
        while (true) {
            System.out.print("Choice [1-" + destAccounts.size() + "]: ");
            try { dIdx = Integer.parseInt(scanner.nextLine()); if (dIdx >= 1 && dIdx <= destAccounts.size()) break; } catch (Exception ignored) {}
            System.out.println("Invalid.");
        }
        Account to = destAccounts.get(dIdx - 1);
        BigDecimal amount;
        while (true) {
            System.out.print("Amount to transfer: ");
            try { amount = new BigDecimal(scanner.nextLine()).setScale(2, RoundingMode.HALF_UP); if (amount.compareTo(BigDecimal.ZERO) > 0) break; } catch (Exception ignored) {}
            System.out.println("Invalid amount.");
        }
        try {
            transactionService.transferOut(from, to, amount);
            System.out.println("Transfer out completed (OUT=PENDING / IN=SETTLED).");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public void History() {
        System.out.println("=========== Transactions History ==========");
        List<Transaction> txs = transactionService.findAll();
        if (txs == null || txs.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.printf("%-36s | %-10s | %-8s | %-13s | %-8s | %-36s | %-36s\n",
                "ID", "AMOUNT", "CURR", "TYPE", "STATUS", "SOURCE_ACC", "TARGET_ACC");
        System.out.println("-".repeat(170));
        for (Transaction t : txs) {
            String src = t.getSource_account_id() != null && t.getSource_account_id().getId() != null ? t.getSource_account_id().getId().toString() : "-";
            String tgt = t.getTarget_account_id() != null && t.getTarget_account_id().getId() != null ? t.getTarget_account_id().getId().toString() : "-";
            System.out.printf("%-36s | %-10s | %-8s | %-13s | %-8s | %-36s | %-36s\n",
                    t.getId(),
                    t.getAmount(),
                    t.getCurrency() != null ? t.getCurrency().name() : "MAD",
                    t.getType() != null ? t.getType().name() : "-",
                    t.getStatus() != null ? t.getStatus().name() : "-",
                    src,
                    tgt);
        }
    }
}

package com.demo1.Controllers;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Models.Credit;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;
import com.demo1.Services.CreditService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CreditController {
    private static final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;
    private final AccountService accountService;
    private final CreditService creditService;

    public CreditController(ClientService clientService, AccountService accountService, CreditService creditService){
        this.clientService = clientService;
        this.accountService = accountService;
        this.creditService = creditService;
    }

    public void RequestCredit(){
        System.out.println("=========== Request Credit ==========");
        System.out.print("Enter client CIN: ");
        String cin = scanner.nextLine().trim().toUpperCase();
        if (cin.isEmpty()) { System.out.println("CIN is required."); return; }
        Client client = clientService.findByCin(cin);
        if (client == null) { System.out.println("Client not found for CIN: " + cin); return; }

        BigDecimal amount;
        while (true) {
            System.out.print("Credit amount: ");
            String in = scanner.nextLine().trim();
            try {
                amount = new BigDecimal(in).setScale(2, RoundingMode.HALF_UP);
                if (amount.compareTo(BigDecimal.ZERO) > 0) break;
                System.out.println("Amount must be > 0.");
            } catch (Exception e) { System.out.println("Invalid number."); }
        }

        int months;
        while (true) {
            System.out.print("Duration in months (e.g. 12): ");
            String in = scanner.nextLine().trim();
            try { months = Integer.parseInt(in); if (months >= 1) break; } catch (Exception ignored) {}
            System.out.println("Invalid duration. Must be >= 1.");
        }

        List<Account> accounts = accountService.findByClient(client);
        if (accounts == null || accounts.isEmpty()) { System.out.println("No accounts found for this client."); return; }

        List<Account> targetAccounts = accounts.stream()
                .filter(a -> a.getType() != Account.AccountType.CREDIT)
                .toList();
        if (targetAccounts.isEmpty()) { System.out.println("No eligible account (CURRENT/SAVINGS) to receive credit."); return; }

        System.out.println("Select target account:");
        for (int i = 0; i < targetAccounts.size(); i++) {
            Account a = targetAccounts.get(i);
            System.out.printf("%d) %s | %s | Balance: %s %s%n", i + 1, a.getAccountNumber(), a.getType(), a.getBalance(), a.getCurrency() != null ? a.getCurrency().name() : "MAD");
        }
        int choice;
        while (true) {
            System.out.print("Enter choice [1-" + targetAccounts.size() + "]: ");
            String input = scanner.nextLine().trim();
            try { choice = Integer.parseInt(input); if (choice >= 1 && choice <= targetAccounts.size()) break; } catch (NumberFormatException ignored) {}
            System.out.println("Invalid choice. Try again.");
        }
        Account targetAccount = targetAccounts.get(choice - 1);

        try {
            Credit credit = creditService.createCredit(client, targetAccount, amount, Credit.CreditType.SIMPLE, months);
            System.out.println("Credit request saved (status = PENDING). Waiting manager approval.");
            System.out.println("Credit ID: " + credit.getId());
            System.out.println("Amount: " + credit.getAmount());
            System.out.println("Rate: 5% (fixed) | Duration: " + months + " months");
            System.out.println("Monthly (approx): " + creditService.getLastMonthlyPayment());
        } catch (Exception e) {
            System.out.println("Credit creation failed: " + e.getMessage());
        }
    }

    public void listPendingCredits() {
        System.out.println("=========== Pending Credits ===========");
        List<Credit> pending = creditService.listPending();
        if (pending == null || pending.isEmpty()) { System.out.println("No pending credits."); return; }
        for (Credit c : pending) {
            int months = 1;
            if (c.getStartDate() != null && c.getEndDate() != null) {
                months = Math.max(1, (int) java.time.temporal.ChronoUnit.MONTHS.between(c.getStartDate(), c.getEndDate()));
            }
            BigDecimal interest = c.getAmount().multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal monthly = c.getAmount().add(interest).divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
            System.out.println("ID: " + c.getId());
            System.out.println("  Amount: " + c.getAmount());
            System.out.println("  Months: " + months + " | Monthly: " + monthly);
            System.out.println("  Status: " + c.getStatus());
            System.out.println("  Account: " + (c.getAccount() != null ? c.getAccount().getAccountNumber() : "-") );
            System.out.println("-------------------------------------");
        }
    }

    public void ApproveCredits() {
        listPendingCredits();
        System.out.print("Enter Credit ID to approve (blank to cancel): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) { System.out.println("Cancelled."); return; }
        try {
            UUID creditId = UUID.fromString(id);
            Credit approved = creditService.approveCredit(creditId);
            if (approved != null) {
                System.out.println("Approved: account credited.");
            } else {
                System.out.println("Credit not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID.");
        } catch (Exception e) {
            System.out.println("Approval failed: " + e.getMessage());
        }
    }
}

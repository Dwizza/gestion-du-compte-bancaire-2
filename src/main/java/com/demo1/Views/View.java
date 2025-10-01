package com.demo1.Views;

import java.util.Scanner;

public class View {
    private static final Scanner sc = new Scanner(System.in);

    public static void showLoginMenu() {
        System.out.println("=== Welcome to Digital Banking ===");
        System.out.println("1. Login");
        System.out.println("0. Exit");
    }

//    public static void showMainMenu(String userName, String role) {
//        System.out.println("\nLogged in as [" + userName + "] - Role: " + role);
//        System.out.println("1. Accounts");
//        System.out.println("2. Transactions");
//        System.out.println("3. Fees & Commissions");
//        System.out.println("4. Credits");
//        System.out.println("5. Reports & Statistics");
//        System.out.println("6. Profile");
//        System.out.println("7. Logout");
//        System.out.println("0. Exit");
//    }

    public static void showBackMenu() {
        System.out.println("9. Back");
        System.out.println("0. Exit");
    }

    public static boolean confirmAction(String message) {
        System.out.print(message + " (y/n): ");
        String input = sc.nextLine();
        return input.equalsIgnoreCase("y");
    }

    public static void showAdminMenu() {
        System.out.println("1. User Management");
        System.out.println("2. Client Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Force Account Closure");
        System.out.println("5. View All Operations");
        showBackMenu();
    }

    public static void showTellerMenu() {
        System.out.println("1. Client Management");
        System.out.println("2. Create Account");
        System.out.println("3. Transaction Management");
        System.out.println("6. Credit Request");
        showBackMenu();
    }

    public static void showManagerMenu() {
        System.out.println("1. View Clients");
        System.out.println("2. View Accounts");
        System.out.println("3. Approve Account Closure");
        System.out.println("4. Approve Credits");
        System.out.println("5. Approve External Transfers");
        System.out.println("6. Monitor Transactions");
        showBackMenu();
    }

    public static void showAuditorMenu() {
        System.out.println("1. View Clients");
        System.out.println("2. View Accounts");
        System.out.println("3. View Transactions");
        System.out.println("4. View Credits");
        System.out.println("5. Generate Audit Report");
        showBackMenu();
    }

    public static void showFeesMenu() {
        System.out.println("1. List Fee Rules");
        System.out.println("2. Add Rule");
        System.out.println("3. Update Rule");
        System.out.println("4. Activate/Deactivate Rule");
        System.out.println("5. Fees Revenue (day, month, period)");
        showBackMenu();
    }

    public static void showReportsMenu() {
        System.out.println("1. Bank Total Balance");
        System.out.println("2. Credit Revenues");
        System.out.println("3. Top Clients by Volume");
        System.out.println("4. Export CSV/TXT");
        showBackMenu();
    }

    public static void showAccountsMenu() {
        System.out.println("1. Create Account");
        System.out.println("2. List My Accounts");
        System.out.println("3. Update Profile");
        System.out.println("4. Change Password");
        System.out.println("5. Close Account");
        showBackMenu();
    }

    public static void showTransactionsMenu() {
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Transaction History");
        showBackMenu();
    }

    public static void showCreditsMenu() {
        System.out.println("1. Request Credit");
        System.out.println("2. Credit Follow-up");
        System.out.println("3. Repayment");
        showBackMenu();
    }

    public static void showProfileMenu() {
        System.out.println("1. Update Profile");
        System.out.println("2. Change Password");
        showBackMenu();
    }

    public static void showUserManagementMenu() {
        System.out.println("=== User Management ===");
        System.out.println("1. Create User");
        System.out.println("2. Edit User");
        System.out.println("3. Delete User");
        System.out.println("4. List Users");
        showBackMenu();
    }

    public static void showClientManagementMenu() {
        System.out.println("=== Client Management ===");
        System.out.println("1. Create Client");
        System.out.println("2. Edit Client");
        System.out.println("3. Delete Client");
        System.out.println("4. List Clients");
        showBackMenu();
    }
}

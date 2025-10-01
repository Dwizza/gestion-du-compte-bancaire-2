package com.demo1;

import com.demo1.Controllers.AccountController;
import com.demo1.Controllers.TransactionController;
import com.demo1.Controllers.UserController;
import com.demo1.Controllers.ClientController;
import com.demo1.Views.View;

import java.util.Scanner;

public class Menus {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void showAdminMenu() {
        View.showAdminMenu();
    }

    public static void showTellerMenu() {
        View.showTellerMenu();
    }

    public static void showManagerMenu() {
        View.showManagerMenu();
    }

    public static void showAuditorMenu() {
        View.showAuditorMenu();
    }

    public static void choiceMenuAdmin(int ms) {
        switch (ms) {
            case 1:
                // User Management sub-menu
                View.showUserManagementMenu();
                System.out.print("choice: ");
                int sub = readInt();
                switch (sub) {
                    case 1 -> UserController.createUser();
                    case 2 -> UserController.editUser();
                    case 3 -> UserController.deleteUser();
                    case 4 -> UserController.listUsers();
                    case 9 -> { /* back */ }
                    case 0 -> { System.out.println("Exit"); System.exit(0);}
                    default -> View.showUserManagementMenu();
                }
                break;
            case 2:
                // Client Management sub-menu (Admin)
                View.showClientManagementMenu();
                System.out.print("choice: ");
                int sub2 = readInt();
                switch (sub2) {
                    case 1 -> ClientController.saveClient();
                    case 2 -> ClientController.editClient();
                    case 3 -> ClientController.deleteClient();
                    case 4 -> ClientController.listClients();
                    case 9 -> { /* back */ }
                    case 0 -> { System.out.println("Exit"); System.exit(0);}
                    default -> View.showClientManagementMenu();
                }
                break;
            case 3:
                View.showTransactionsMenu();
                System.out.print("choice: ");
                int sub3 = readInt();
                switch (sub3) {
                    case 1 -> TransactionController.Deposit();
                    case 2 -> TransactionController.Withdraw();
                    case 9 -> { /* back */ }
                    case 0 -> { System.out.println("Exit"); System.exit(0);}
                    default -> View.showTransactionsMenu();
                }
                break;
            case 4:
                // Force account closure (to implement with MANAGER validation)
                break;
            case 5:
                // View all operations (to implement)
                break;
            case 0:
                System.out.println("Exit");
                System.exit(0);
                break;
            case 9:
                // Back
                break;
            default:
                showAdminMenu();
                break;
        }
    }

    public static void choiceMenuTeller(int ms) {
        switch (ms) {
            case 1:
                // Client Management sub-menu
                View.showClientManagementMenu();
                System.out.print("choice: ");
                int sub = readInt();
                switch (sub) {
                    case 1 -> ClientController.saveClient();
                    case 2 -> ClientController.editClient();
                    case 3 -> ClientController.deleteClient();
                    case 4 -> ClientController.listClients();
                    case 9 -> { /* back */ }
                    case 0 -> { System.out.println("Exit"); System.exit(0);}
                    default -> View.showClientManagementMenu();
                }
                break;
            case 2:
                AccountController.saveAccount();
                break;
            case 3:
                View.showTransactionsMenu();
                System.out.print("choice: ");
                int sub1 = readInt();
                switch (sub1) {
                    case 1 -> TransactionController.Deposit();
                    case 2 -> TransactionController.Withdraw();
                    case 9 -> { /* back */ }
                    case 0 -> { System.out.println("Exit"); System.exit(0);}
                    default -> View.showTransactionsMenu();
                }
                break;
            case 4:
                // Withdraw (shortcut) — use transactions menu
                break;
            case 5:
                // Internal transfer
                break;
            case 6:
                // Credit request
                break;
            case 9:
                // Back
                break;
            case 0:
                System.out.println("Exit");
                System.exit(0);
                break;
            default:
                showTellerMenu();
                break;
        }
    }

    public static void choiceMenuManager(int ms) {
        switch (ms) {
            case 1 -> { /* View clients */ }
            case 2 -> { /* View accounts */ }
            case 3 -> { /* Approve account closure */ }
            case 4 -> { /* Approve credits */ }
            case 5 -> { /* Approve external transfers */ }
            case 6 -> { /* Monitor transactions */ }
            case 9 -> { /* Back */ }
            case 0 -> { System.out.println("Exit"); System.exit(0);}
            default -> showManagerMenu();
        }
    }

    public static void choiceMenuAuditor(int ms) {
        switch (ms) {
            case 1 -> { /* View clients */ }
            case 2 -> { /* View accounts */ }
            case 3 -> { /* View transactions */ }
            case 4 -> { /* View credits */ }
            case 5 -> { /* Generate audit report */ }
            case 9 -> { /* Back */ }
            case 0 -> { System.out.println("Exit"); System.exit(0);}
            default -> showAuditorMenu();
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }
}

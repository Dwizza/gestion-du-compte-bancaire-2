package com.demo1;

import com.demo1.Controllers.*;
import com.demo1.Views.View;

import java.util.Scanner;

public class Menus {

    private static final Scanner SCANNER = new Scanner(System.in);

    private final UserController userController;
    private final ClientController clientController;
    private final AccountController accountController;
    private final TransactionController transactionController;
    private final CreditController creditController;

    public Menus(UserController userController, ClientController clientController, AccountController accountController, TransactionController transactionController, CreditController creditController) {
        this.userController = userController;
        this.clientController = clientController;
        this.accountController = accountController;
        this.transactionController = transactionController;
        this.creditController = creditController;
    }


    public static void showAdminMenu() { View.showAdminMenu(); }
    public static void showTellerMenu() { View.showTellerMenu(); }
    public static void showManagerMenu() { View.showManagerMenu(); }
    public static void showAuditorMenu() { View.showAuditorMenu(); }

    public void choiceMenuAdmin(int ms) {
        switch (ms) {
            case 1 -> {
                View.showUserManagementMenu();
                System.out.print("choice: ");
                int sub = readInt();
                switch (sub) {
                    case 1 -> userController.createUser();
                    case 2 -> userController.editUser();
                    case 3 -> userController.deleteUser();
                    case 4 -> userController.listUsers();
                    case 9, 0 -> {}
                }
            }
            case 2 -> {
                View.showClientManagementMenu();
                System.out.print("choice: ");
                int sub2 = readInt();
                switch (sub2) {
                    case 1 -> clientController.saveClient();
                    case 2 -> clientController.editClient();
                    case 3 -> clientController.deleteClient();
                    case 4 -> clientController.listClients();
                    case 9, 0 -> {}
                }
            }
            case 3 -> {
                View.showTransactionsMenu();
                System.out.print("choice: ");
                int sub3 = readInt();
                switch (sub3) {
                    case 1 -> transactionController.Deposit();
                    case 2 -> transactionController.Withdraw();
                    case 3 -> transactionController.TransferInternal();
                    case 4 -> transactionController.TransferOut();
                    case 5 -> transactionController.History();
                    case 9, 0 -> {}
                }
            }
            case 4, 5, 9, 0 -> {}
            default -> showAdminMenu();
        }
    }

    public void choiceMenuTeller(int ms) {
        switch (ms) {
            case 1 -> {
                View.showClientManagementMenu();
                System.out.print("choice: ");
                int sub = readInt();
                switch (sub) {
                    case 1 -> clientController.saveClient();
                    case 2 -> clientController.editClient();
                    case 3 -> clientController.deleteClient();
                    case 4 -> clientController.listClients();
                    case 9, 0 -> {}
                }
            }
            case 2 -> accountController.saveAccount();
            case 3 -> {
                View.showTransactionsMenuTeller();
                System.out.print("choice: ");
                int sub1 = readInt();
                switch (sub1) {
                    case 1 -> transactionController.Deposit();
                    case 2 -> transactionController.Withdraw();
                    case 3 -> transactionController.TransferInternal();
                    case 4 -> transactionController.TransferOut();
                    case 9, 0 -> {}
                }
            }
            case 4 -> creditController.RequestCredit();
            case  5, 6, 9, 0 -> {}
            default -> showTellerMenu();
        }
    }

    public void choiceMenuManager(int ms) {
        switch (ms) {
            case 4 -> creditController.ApproveCredits();
            case 1,2,3,5,6,9,0 -> {}
            default -> showManagerMenu();
        }
    }

    public void choiceMenuAuditor(int ms) {
        switch (ms) {
            case 1,2 -> {}
            case 3 -> transactionController.History();
            case 4,5,9,0 -> {}
            default -> showAuditorMenu();
        }
    }

    private static int readInt() {
        while (true) {
            try { return Integer.parseInt(SCANNER.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Invalid number, try again: "); }
        }
    }
}

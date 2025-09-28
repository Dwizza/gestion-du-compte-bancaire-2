package com.demo1;

import com.demo1.Controllers.AuthController;
import com.demo1.Views.View;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Welcome to Digital Banking ===");
        while (true) {
            View.showLoginMenu();
            System.out.print("choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    // Start authentication: ask email + password
                    AuthController.Login();
                    break;
                case "0":
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    public static void showMainMenu(String userName, String role) {
        boolean running = true;
        while (running) {
            View.showMainMenu(userName, role);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    View.showAccountsMenu();
                    // Call accounts menu
                    break;
                case "2":
                    View.showTransactionsMenu();
                    // Call transactions menu
                    break;
                case "3":
                    View.showFeesMenu();
                    // Call fees menu
                    break;
                case "4":
                    View.showCreditsMenu();
                    // Call credits menu
                    break;
                case "5":
                    View.showReportsMenu();
                    // Call reports menu
                    break;
                case "6":
                    View.showProfileMenu();
                    // Call profile menu
                    break;
                case "7":
                    running = false; // Logout
                    break;
                case "0":
                    System.out.println("Good bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}

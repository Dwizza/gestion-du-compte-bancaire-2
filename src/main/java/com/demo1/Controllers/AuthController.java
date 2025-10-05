package com.demo1.Controllers;

import com.demo1.Menus;
import com.demo1.Models.User;
import com.demo1.Services.AuthService;
import com.demo1.Exceptions.BusinessRuleViolationException;

import java.util.Scanner;

public class AuthController {

    private static final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;
    private final Menus menus;

    public AuthController(AuthService authService, Menus menus) {
        this.authService = authService;
        this.menus = menus;
    }

    public void Login(){
        while (true) {
            System.out.println("======== Login ========");
            System.out.print("email : ");
            String loginEmail = scanner.nextLine().trim().toLowerCase();
            System.out.print("password : ");
            String password = scanner.nextLine();
            try {
                User loggedIn = authService.Login(loginEmail, password);
                if (loggedIn != null) {
                    while (true) {
                        System.out.println("\n=== MENU " + loggedIn.getRole() + " ===");
                        switch (loggedIn.getRole()) {
                            case ADMIN:
                                Menus.showAdminMenu();
                                System.out.print("choice: ");
                                int choice1 = readInt();
                                menus.choiceMenuAdmin(choice1);
                                break;
                            case TELLER:
                                Menus.showTellerMenu();
                                System.out.print("choice: ");
                                int choice2 = readInt();
                                menus.choiceMenuTeller(choice2);
                                break;
                            case MANAGER:
                                Menus.showManagerMenu();
                                System.out.print("choice: ");
                                int choice3 = readInt();
                                menus.choiceMenuManager(choice3);
                                break;
                            case AUDITOR:
                                Menus.showAuditorMenu();
                                System.out.print("choice: ");
                                int choice4 = readInt();
                                menus.choiceMenuAuditor(choice4);
                                break;
                        }
                    }
                } else {
                    System.out.println("Invalid credentials. Please try again.\n");
                }
            } catch (BusinessRuleViolationException ex) {
                System.out.println(ex.getMessage());
                System.out.println();
            }
        }
    }

    private static int readInt() {
        while (true) {
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                System.out.print("Nombre invalide, réessayez: ");
            }
        }
    }
}

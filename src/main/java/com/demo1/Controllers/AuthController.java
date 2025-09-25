package com.demo1.Controllers;

import com.demo1.Menus;
import com.demo1.Models.User;
import com.demo1.Services.AuthService;
import com.demo1.Services.impliment.AuthServiceImplement;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.Scanner;

public class AuthController {

    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService = new AuthServiceImplement();

     public static void Login(){
         System.out.println("========Login========");
         System.out.print("email : ");
         String loginEmail = scanner.nextLine();
         System.out.print("password : ");
         String password = scanner.nextLine();

         User loggedIn = authService.Login(loginEmail, password);

         if (loggedIn != null) {
             boolean running = true;

             while (running) {
                 System.out.println("\n=== MENU " + loggedIn.getRole() + " ===");

                 switch (loggedIn.getRole()) {
                     case ADMIN:
                         Menus.showAdminMenu();
                         System.out.print("choice: ");
                         int choice1 = scanner.nextInt();
                         Menus.choiceMenuAdmin(choice1);
                         break;
                     case TELLER:
                         Menus.showTellerMenu();
                         System.out.println("choice: ");
                         int choice2 = scanner.nextInt();
                         Menus.choiceMenuTeller(choice2);
                         break;

                     case MANAGER:
                         Menus.showManagerMenu();
                         System.out.println("choice: ");
                         int choice3 = scanner.nextInt();
                         Menus.choiceMenuManager(choice3);
                         break;

                     case AUDITOR:
                         Menus.showAuditorMenu();
                         System.out.println("choice: ");
                         int choice4 = scanner.nextInt();
                         Menus.choiceMenuAuditor(choice4);
                         break;
                 }
             }
         } else {
             System.out.println("Login failed !");
         }

         scanner.close();
     }
    }


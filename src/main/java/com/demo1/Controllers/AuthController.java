package com.demo1.Controllers;

import com.demo1.Models.User;
import com.demo1.Services.AuthService;
import com.demo1.Services.impliment.AuthServiceImplement;

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
                         System.out.println("1. Gestion utilisateurs");
                         System.out.println("2. Configuration système");
                         System.out.println("3. Clôture forcée de comptes");
                         System.out.println("4. Modifier paramètres globaux");
                         System.out.println("5. Consulter toutes les opérations");
                         System.out.println("0. Quitter");

                         System.out.print("choice: ");
                         int choice = scanner.nextInt();



                     case MANAGER:
                         System.out.println("1. Consulter clients");
                         System.out.println("2. Consulter comptes");
                         System.out.println("3. Valider clôture de compte");
                         System.out.println("4. Valider crédits");
                         System.out.println("5. Valider virements externes");
                         System.out.println("6. Suivi transactions");
                         System.out.println("0. Quitter");
                         break;

                     case TELLER:
                         System.out.println("1. Créer un client");
                         System.out.println("2. Consulter un client");
                         System.out.println("3. Dépôt d’argent");
                         System.out.println("4. Retrait d’argent");
                         System.out.println("5. Virement interne");
                         System.out.println("6. Demande de crédit");
                         System.out.println("0. Quitter");
                         break;

                     case AUDITOR:
                         System.out.println("1. Consulter clients");
                         System.out.println("2. Consulter comptes");
                         System.out.println("3. Consulter transactions");
                         System.out.println("4. Consulter crédits");
                         System.out.println("5. Générer rapports d’audit");
                         System.out.println("0. Quitter");
                         break;
                 }
             }
         } else {
             System.out.println("Login failed !");
         }

         scanner.close();
     }
    }


package com.demo1;

import com.demo1.Controllers.AuthController;
import com.demo1.Views.View;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

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



}

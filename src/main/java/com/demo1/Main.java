package com.demo1;

import com.demo1.Controllers.AuthController;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("=== Bienvenue dans la Banque Digitale ===");
        showLoginMenu();
    }

    public static void showLoginMenu(){
        System.out.println("=====Menu Principale======");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine();

        switch(choice){
            case"1":
                AuthController.Login();
                break;
            case "2":
                System.out.println("Good bye!");
                System.exit(0);
                break;
            default:
                showLoginMenu();
                break;
        }
    }


}



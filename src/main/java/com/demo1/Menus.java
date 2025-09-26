package com.demo1;

import com.demo1.Controllers.UserController;
import com.demo1.Models.User;

import java.util.Scanner;

public class Menus {

    private static  Scanner sc = new Scanner(System.in);

    public static void showAdminMenu(){
        System.out.println("1. Create User");
        System.out.println("2. Edit User");
        System.out.println("3. Delete User");
        System.out.println("4. System Configuration");
        System.out.println("5. Force Account Closure");
        System.out.println("6. View All Operations");
        System.out.println("0. Exit");
    }

    public static void showTellerMenu(){
        System.out.println("1. Create Client");
        System.out.println("2. View Client");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Internal Transfer");
        System.out.println("6. Credit Request");
        System.out.println("0. Exit");
    }

    public static void showManagerMenu(){
        System.out.println("1. View Clients");
        System.out.println("2. View Accounts");
        System.out.println("3. Approve Account Closure");
        System.out.println("4. Approve Credits");
        System.out.println("5. Approve External Transfers");
        System.out.println("6. Monitor Transactions");
        System.out.println("0. Exit");
    }

    public static void showAuditorMenu(){
        System.out.println("1. View Clients");
        System.out.println("2. View Accounts");
        System.out.println("3. View Transactions");
        System.out.println("4. View Credits");
        System.out.println("5. Generate Audit Reports");
        System.out.println("0. Exit");
    }


    public static void choiceMenuAdmin(int ms){
        switch(ms){
            case 1:
                UserController.createUser();
                break;
            case 2:
                UserController.editUser();
                break;
            case 3:
                UserController.deleteUser();
                break;
            default:
                showAdminMenu();
                break;
        }

    }

    public static void choiceMenuTeller(int ms){

    }

    public static void choiceMenuManager(int ms){

    }

    public static void choiceMenuAuditor(int ms){

    }

}

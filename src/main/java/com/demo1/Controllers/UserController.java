package com.demo1.Controllers;

import com.demo1.Menus;
import com.demo1.Models.User;
import com.demo1.Services.UserService;
import com.demo1.Services.impliment.UserServiceImplement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserController {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImplement();

    public static void createUser(){
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println("Role [1-TELLER, 2-MANAGER, 3-ADMIN, 4-AUDITOR]: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        User.Role role = switch (roleChoice) {
            case 1 -> User.Role.TELLER;
            case 2 -> User.Role.MANAGER;
            case 3 -> User.Role.ADMIN;
            case 4 -> User.Role.AUDITOR;
            default -> User.Role.TELLER;
        };
        userService.createUser(name, email, password, role);

    }

    public static void editUser(){
        System.out.print("email: ");
        String reccentEmail = scanner.nextLine();

        User user = userService.findUser(reccentEmail);
        if(user != null ){
            System.out.println("Edit User");
            System.out.print("name: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String Email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Role [1-TELLER, 2-MANAGER, 3-ADMIN, 4-AUDITOR]: ");
            int roleChoice = scanner.nextInt();
            scanner.nextLine();

            User.Role role = switch (roleChoice) {
                case 1 -> User.Role.TELLER;
                case 2 -> User.Role.MANAGER;
                case 3 -> User.Role.ADMIN;
                case 4 -> User.Role.AUDITOR;
                default -> User.Role.TELLER;
            };

            userService.editUser(name, Email, password, role, reccentEmail);

        }
    }

    public static void deleteUser(){
        System.out.print("email: ");
        String recentEmail = scanner.nextLine();

        User user = userService.findUser(recentEmail);
        if(user != null){
            userService.deleteUser(user);
        }else{
            System.out.println("User is not existed");
        }
    }

    public static void findUser(){
        List<User> users = new ArrayList<>();
        users = userService.findUser();
        users.stream().forEach(System.out::println);
    }
}

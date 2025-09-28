package com.demo1.Controllers;

import com.demo1.Models.User;
import com.demo1.Services.UserService;
import com.demo1.Services.impliment.UserServiceImplement;
import com.demo1.Exceptions.BusinessRuleViolationException;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserServiceImplement();

    public static void createUser(){
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
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
            System.out.println("User created successfully!");
        } catch (BusinessRuleViolationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void editUser(){
        System.out.print("email: ");
        String recentEmail = scanner.nextLine().trim();

        User user = userService.findByEmail(recentEmail);
        if(user != null ){
            try {
                System.out.println("Edit User");
                System.out.print("name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Email: ");
                String Email = scanner.nextLine().trim();
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
                userService.editUser(name, Email, password, role, recentEmail);
                System.out.println("User updated successfully!");
            } catch (BusinessRuleViolationException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("User not found.");
        }
    }

    public static void deleteUser(){
        System.out.print("email: ");
        String recentEmail = scanner.nextLine().trim();
        User user = userService.findByEmail(recentEmail);
        if(user != null){
            userService.deleteUser(user);
        } else {
            System.out.println("User is not existed");
        }
    }

    public static void listUsers(){
        List<User> users = userService.findAll();
        users.forEach(System.out::println);
    }
}

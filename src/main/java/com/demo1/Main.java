package com.demo1;

import com.demo1.Controllers.*;
import com.demo1.Repository.implement.*;
import com.demo1.Repository.*;
import com.demo1.Services.*;
import com.demo1.Services.impliment.*;
import com.demo1.Views.View;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImplement();
        AccountRepository accountRepository = new AccountRepositoryImplement();
        ClientRepository clientRepository = new ClientRepositoryImplement();
        TransactionRepository transactionRepository = new TransactionRepositoryImplement();

        UserService userService = new UserServiceImplement(userRepository);
        AccountService accountService = new AccountServiceImplement(accountRepository);
        ClientService clientService = new ClientServiceImplement(clientRepository, accountService);
        TransactionService transactionService = new TransactionServiceImplement(transactionRepository);
        AuthService authService = new AuthServiceImplement(userRepository);

        UserController userController = new UserController(userService);
        ClientController clientController = new ClientController(clientService);
        AccountController accountController = new AccountController(clientService, accountService);
        TransactionController transactionController = new TransactionController(clientService, accountService, transactionService);

        Menus menus = new Menus(userController, clientController, accountController, transactionController);
        AuthController authController = new AuthController(authService, menus);

        while (true) {
            View.showLoginMenu();
            System.out.print("choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    authController.Login();
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

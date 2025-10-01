package com.demo1.Controllers;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Models.User;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;
import com.demo1.Services.impliment.AccountServiceImplement;
import com.demo1.Services.impliment.ClientServiceImplement;


import java.math.BigDecimal;
import java.util.Scanner;

public class AccountController {
    private static Scanner scanner = new Scanner(System.in);
    private static final ClientService clientService = new ClientServiceImplement();
    private static final AccountService accountService = new AccountServiceImplement();

    public static void saveAccount(){

        System.out.println("=== Create account ===");
        System.out.print("Enter client email: ");
        String email = scanner.nextLine();
        System.out.print("Your Balance: ");
        BigDecimal balance = scanner.nextBigDecimal();
        System.out.print("Account Type [ 1-SAVING, 2-CREDIT]: ");
        int type = scanner.nextInt();

        Account.AccountType accountType = switch (type) {
            case 1 -> Account.AccountType.SAVINGS;
            case 2 -> Account.AccountType.CREDIT;
            default -> Account.AccountType.CURRENT;
        };

        Client client = clientService.findByEmail(email);
        if(client != null){
            try{
                accountService.saveAccount(client, balance, accountType);
                System.out.println("Account Created Successfully !");
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Client is not exist !");
        }
    }

    public static Client AccountByClient(){
        System.out.println("Enter valid Email: ");
        String email = scanner.nextLine();

        Client client = clientService.findByEmail(email);

        if (client != null) {
            Account account = accountService.AccountByClient(client);
            System.out.println(account);
            return client;
        }else {
            System.out.println("Email not Found");
        }
        return null;
    }
}

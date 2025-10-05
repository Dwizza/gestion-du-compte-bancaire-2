package com.demo1.Controllers;

import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AccountController {
    private static final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService;
    private final AccountService accountService;

    public AccountController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    public void saveAccount(){

        System.out.println("=== Create account ===");
        System.out.print("Enter client email: ");
        String email = scanner.nextLine();
        System.out.print("Your Balance: ");
        BigDecimal balance = new BigDecimal(scanner.nextLine());
        System.out.print("Account Type [ 1-SAVING, 2-CREDIT]: ");
        int type = Integer.parseInt(scanner.nextLine());

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

    public Client AccountByClient(){
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

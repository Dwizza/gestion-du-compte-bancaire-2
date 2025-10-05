package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Repository.ClientRepository;
import com.demo1.Services.AccountService;
import com.demo1.Services.ClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ClientServiceImplement implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public ClientServiceImplement(ClientRepository clientRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
    }

    public Client save(String fullName, String address, String email, Double salary, Client.Currency currency, String cin){
        validateClientFields(fullName, address, email, salary, currency);
        validateCin(cin);
        if (clientRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Client email already in use.");
        }
        if (clientRepository.findByCin(cin) != null) {
            throw new BusinessRuleViolationException("Client CIN already in use.");
        }
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFullName(fullName.trim());
        client.setAddress(address.trim());
        client.setEmail(email.trim());
        client.setSalary(BigDecimal.valueOf(salary));
        client.setCurrency(currency);
        client.setCin(cin.trim().toUpperCase());
        clientRepository.saveClient(client);
        accountService.saveAccount(client, BigDecimal.valueOf(0.00), Account.AccountType.CURRENT);

        return client;
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void editClient(String fullName, String address, String email, Double salary, Client.Currency currency, String cin, String recentEmail) {
        validateClientFields(fullName, address, email, salary, currency);
        validateCin(cin);
        if (!email.equalsIgnoreCase(recentEmail) && clientRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Client email already in use.");
        }
        var existingCin = clientRepository.findByCin(cin.trim().toUpperCase());
        if (existingCin != null && !existingCin.getEmail().equalsIgnoreCase(recentEmail)) {
            throw new BusinessRuleViolationException("Client CIN already in use.");
        }
        Client client = new Client();
        client.setFullName(fullName.trim());
        client.setAddress(address.trim());
        client.setEmail(email.trim());
        client.setSalary(BigDecimal.valueOf(salary));
        client.setCurrency(currency);
        client.setCin(cin.trim().toUpperCase());
        clientRepository.updateClient(client, recentEmail);
    }

    @Override
    public void deleteClient(Client client) {
        clientRepository.deleteClient(client);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByCin(String cin) {
        if (cin == null) return null;
        return clientRepository.findByCin(cin.trim().toUpperCase());
    }

    private void validateClientFields(String fullName, String address, String email, Double salary, Client.Currency currency) {
        if (fullName == null || fullName.isBlank()) {
            throw new BusinessRuleViolationException("Full name is required.");
        }
        if (address == null || address.isBlank()) {
            throw new BusinessRuleViolationException("Address is required.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessRuleViolationException("Invalid email format.");
        }
        if (salary == null || BigDecimal.valueOf(salary).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException("Salary must be strictly positive.");
        }
        if (currency == null) {
            throw new BusinessRuleViolationException("Currency is required.");
        }
    }

    private void validateCin(String cin){
        if (cin == null || cin.trim().isEmpty()) {
            throw new BusinessRuleViolationException("CIN is required.");
        }
        if (!cin.trim().matches("^[A-Za-z0-9]{4,20}$")) {
            throw new BusinessRuleViolationException("CIN must be 4-20 alphanumeric characters.");
        }
    }
}

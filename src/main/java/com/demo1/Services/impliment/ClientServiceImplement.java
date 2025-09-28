package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.Client;
import com.demo1.Repository.ClientRepository;
import com.demo1.Repository.implement.ClientRepositoryImplement;
import com.demo1.Services.ClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ClientServiceImplement implements ClientService {

    private static final ClientRepository clientRepository = new ClientRepositoryImplement();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Client save(String fullName, String address, String email, Double salary, Client.Currency currency){
        validateClientFields(fullName, address, email, salary, currency);
        if (clientRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Client email already in use.");
        }
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFullName(fullName.trim());
        client.setAddress(address.trim());
        client.setEmail(email.trim());
        client.setSalary(BigDecimal.valueOf(salary));
        client.setCurrency(currency);
        clientRepository.saveClient(client);
        return client;
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void editClient(String fullName, String address, String email, Double salary, Client.Currency currency, String recentEmail) {
        validateClientFields(fullName, address, email, salary, currency);
        if (!email.equalsIgnoreCase(recentEmail) && clientRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Client email already in use.");
        }
        Client client = new Client();
        client.setFullName(fullName.trim());
        client.setAddress(address.trim());
        client.setEmail(email.trim());
        client.setSalary(BigDecimal.valueOf(salary));
        client.setCurrency(currency);
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
}

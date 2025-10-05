package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Services.AuthService;

import java.util.regex.Pattern;

public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public AuthServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User Login(String email, String password){
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessRuleViolationException("Invalid email format.");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessRuleViolationException("Password is required.");
        }
        User user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}

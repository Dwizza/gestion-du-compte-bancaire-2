package com.demo1.Services.impliment;

import com.demo1.Exceptions.BusinessRuleViolationException;
import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Repository.implement.UserRepositoryImplement;
import com.demo1.Services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class UserServiceImplement implements UserService {

    private static final UserRepository userRepository = new UserRepositoryImplement();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void createUser(String name, String email, String password, User.Role role){
        validateUserFields(name, email, password, role);
        if (userRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Email already in use.");
        }
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password);
        user.setRole(role);
        try{
            userRepository.saveUser(user);
        }catch (Exception e){
            throw new BusinessRuleViolationException("Unable to create user: " + e.getMessage());
        }
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public void editUser(String name, String email, String password, User.Role role, String recentEmail){
        validateUserFields(name, email, password, role);
        if (!email.equalsIgnoreCase(recentEmail) && userRepository.findByEmail(email) != null) {
            throw new BusinessRuleViolationException("Email already in use.");
        }
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password);
        user.setRole(role);
        try {
            userRepository.editUser(user, recentEmail);
        } catch (Exception e) {
            throw new BusinessRuleViolationException("Unable to update user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(User user){
        userRepository.deleteUser(user);
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    private void validateUserFields(String name, String email, String password, User.Role role) {
        if (name == null || name.isBlank()) {
            throw new BusinessRuleViolationException("Name is required.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessRuleViolationException("Invalid email format.");
        }
        if (password == null || password.length() < 6) {
            throw new BusinessRuleViolationException("Password must be at least 6 characters.");
        }
        if (role == null) {
            throw new BusinessRuleViolationException("Role is required.");
        }
    }
}

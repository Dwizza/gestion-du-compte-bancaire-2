package com.demo1.Services;

import com.demo1.Models.User;

import java.util.List;

public interface UserService {

    void createUser(String name, String email, String password, User.Role role);
    void editUser(String name,String email, String password, User.Role role, String recentEmail);
    User findByEmail(String email);
    void deleteUser(User user);
    List<User> findAll();
}

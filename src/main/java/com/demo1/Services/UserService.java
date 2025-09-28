package com.demo1.Services;

import com.demo1.Models.User;

import java.util.List;
import java.util.ListResourceBundle;

public interface UserService {

    void createUser(String name, String email, String password, User.Role role);
    void editUser(String name,String email, String password, User.Role role, String reccentEmail);
    User findUser(String email);
    void deleteUser(User user);
    List<User> findUser();
}

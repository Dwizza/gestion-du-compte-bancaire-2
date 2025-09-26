package com.demo1.Services;

import com.demo1.Models.User;

public interface UserService {

    void createUser(String name, String email, String password, User.Role role);
    void editUser(String name,String email, String password, User.Role role, String reccentEmail);
    User findUser(String email);
    void deleteUser(User user);
}

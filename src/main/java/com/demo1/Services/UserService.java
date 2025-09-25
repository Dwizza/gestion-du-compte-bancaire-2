package com.demo1.Services;

import com.demo1.Models.User;

public interface UserService {

    void createUser(String name, String email, String password, User.Role role);

}

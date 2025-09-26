package com.demo1.Repository;

import com.demo1.Models.User;

import java.util.Optional;

public interface UserRepository {
    void saveUser(User user);
    User FindByEmail(String email);
    void editUser(User user, String email);
    void deleteUser(User user);
}

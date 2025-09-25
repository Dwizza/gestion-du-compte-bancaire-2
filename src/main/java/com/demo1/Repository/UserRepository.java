package com.demo1.Repository;

import com.demo1.Models.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    User FindByEmail(String email);
}

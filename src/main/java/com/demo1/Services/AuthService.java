package com.demo1.Services;

import com.demo1.Models.User;

public interface AuthService {
    User Login(String email,String password);
}

package com.demo1.Services.impliment;

import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Repository.implement.UserRepositoryImplement;
import com.demo1.Services.AuthService;

public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository = new UserRepositoryImplement();

    public User Login(String email, String password){
        User user = userRepository.FindByEmail(email);
        if(user != null && user.getPassword().equalsIgnoreCase(password)){
            return user;
        }
        return null;
    }

}

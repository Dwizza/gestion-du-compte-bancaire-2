package com.demo1.Services.impliment;

import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Repository.implement.UserRepositoryImplement;
import com.demo1.Services.UserService;

import java.util.UUID;

public class UserServiceImplement implements UserService {

    private static UserRepository userRepository = new UserRepositoryImplement();

    public void createUser(String name, String email, String password, User.Role role){
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        try{
            userRepository.save(user);
            System.out.println("User created successfully !");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}

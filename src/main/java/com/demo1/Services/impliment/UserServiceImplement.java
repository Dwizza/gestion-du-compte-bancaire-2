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
            userRepository.saveUser(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public User findUser(String email){
        User user = userRepository.FindByEmail(email);
        if(user != null){
           return user;
        }else {
            return null;
        }
    }

    public void editUser(String name,String email,String password, User.Role role, String recentEmail){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        userRepository.editUser(user, recentEmail);
    }

    public void deleteUser(User user){
        userRepository.deleteUser(user);
    }

}

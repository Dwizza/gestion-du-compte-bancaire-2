package com.demo1.Repository.implement;

import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Config.DatabaseConfig;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImplement implements UserRepository {

    public void save(User user){

        String sql = "INSERT INTO users (id, full_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, String.valueOf(user.getId()));
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole().name());

            pstmt.execute();
            System.out.println("User Saved Successfully");

        }catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public User FindByEmail(String email){

         String sql =  "SELECT * FROM users WHERE email = ?";
         try(Connection conn = DatabaseConfig.getConnection()){
             PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setString(1, email);

             ResultSet rs = pstmt.executeQuery();
             if(rs.next()){
                 return new User(
                         rs.getString("Name"),
                         rs.getString("Email"),
                         rs.getString("Password"),
                         User.Role.valueOf(rs.getString("Role"))
                 );
             }

         } catch (Exception e) {
             throw new RuntimeException(e);
         }
         return null;
    }
}

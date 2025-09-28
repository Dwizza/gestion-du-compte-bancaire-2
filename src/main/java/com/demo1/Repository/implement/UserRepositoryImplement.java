package com.demo1.Repository.implement;

import com.demo1.Models.User;
import com.demo1.Repository.UserRepository;
import com.demo1.Config.DatabaseConfig;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImplement implements UserRepository {

    @Override
    public void saveUser(User user){

        String sql = "INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setObject(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole().name().toUpperCase());

            pstmt.execute();


        }catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public User findByEmail(String email){

         String sql =  "SELECT * FROM users WHERE email = ?";
         try(Connection conn = DatabaseConfig.getConnection()){
             PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setString(1, email);

             ResultSet rs = pstmt.executeQuery();
             if(rs.next()){
                 User user = new User();
                 user.setId(UUID.fromString(rs.getString("id")));
                 user.setName(rs.getString("name"));
                 user.setEmail(rs.getString("email"));
                 user.setPassword(rs.getString("password"));
                 user.setRole(User.Role.valueOf(rs.getString("role")));

                 return user;
             }

         } catch (Exception e) {
             throw new RuntimeException(e);
         }
         return null;
    }

    @Override
    public void editUser(User user,String email){

        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE email = ?";
        try(Connection conn = DatabaseConfig.getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name().toUpperCase());
            pstmt.setString(5, email);
            pstmt.executeUpdate();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(User user){
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Not found User !");
            }
        }catch(Exception e ){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }

}

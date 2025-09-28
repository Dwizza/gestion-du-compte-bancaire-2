package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Client;
import com.demo1.Models.User;
import com.demo1.Repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class ClientRepositoryImplement implements ClientRepository {

    public void saveClient(Client client){
        String sql = "INSERT INTO clients (id, full_name, address, email, salary, currency) VALUES (?,?,?,?,?,?)";

        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, client.getId());
            pstmt.setString(2, client.getFullName());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getAddress());
            pstmt.setBigDecimal(5, client.getSalary());
            pstmt.setString(6, client.getCurrency().name());
            pstmt.executeUpdate();

            System.out.println("Client Created successfully !");
        }catch(Exception e ){
            throw new RuntimeException("Error saving client", e);
        }
    }
}

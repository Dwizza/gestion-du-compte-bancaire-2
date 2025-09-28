package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Client;
import com.demo1.Repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepositoryImplement implements ClientRepository {

    public void saveClient(Client client){
        String sql = "INSERT INTO clients (id, full_name, address, email, salary, currency) VALUES (?,?,?,?,?,?)";
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, client.getId());
            pstmt.setString(2, client.getFullName());
            pstmt.setString(3, client.getAddress());
            pstmt.setString(4, client.getEmail());
            pstmt.setBigDecimal(5, client.getSalary());
            pstmt.setString(6, client.getCurrency().name());
            pstmt.executeUpdate();

        }catch(Exception e ){
            throw new RuntimeException("Error saving client", e);
        }
    }

    @Override
    public Client findByEmail(String email) {
        String sql = "SELECT id, full_name, address, email, salary, currency FROM clients WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setId(UUID.fromString(rs.getString("id")));
                c.setFullName(rs.getString("full_name"));
                c.setAddress(rs.getString("address"));
                c.setEmail(rs.getString("email"));
                c.setSalary(rs.getBigDecimal("salary"));
                c.setCurrency(Client.Currency.valueOf(rs.getString("currency")));
                return c;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding client by email", e);
        }
    }

    @Override
    public void updateClient(Client client, String recentEmail) {
        String sql = "UPDATE clients SET full_name = ?, address = ?, email = ?, salary = ?, currency = ? WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getFullName());
            ps.setString(2, client.getAddress());
            ps.setString(3, client.getEmail());
            ps.setBigDecimal(4, client.getSalary());
            ps.setString(5, client.getCurrency().name());
            ps.setString(6, recentEmail);
            ps.executeUpdate();
            System.out.println("Client updated successfully !");
        } catch (Exception e) {
            throw new RuntimeException("Error updating client", e);
        }
    }

    @Override
    public void deleteClient(Client client) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, client.getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Client deleted successfully!");
            } else {
                System.out.println("Client not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting client", e);
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT id, full_name, address, email, salary, currency FROM clients";
        List<Client> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Client c = new Client();
                c.setId(UUID.fromString(rs.getString("id")));
                c.setFullName(rs.getString("full_name"));
                c.setAddress(rs.getString("address"));
                c.setEmail(rs.getString("email"));
                c.setSalary(rs.getBigDecimal("salary"));
                c.setCurrency(Client.Currency.valueOf(rs.getString("currency")));
                list.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error listing clients", e);
        }
        return list;
    }
}

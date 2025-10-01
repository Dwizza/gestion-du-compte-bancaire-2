package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Account;
import com.demo1.Models.Client;
import com.demo1.Repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryImplement implements AccountRepository {

        public void saveAccount(Account account, Client client){
            String sql = "INSERT INTO accounts (id, account_number, balance, currency, type, client_id, status)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?)";
            try(Connection conn = DatabaseConfig.getConnection()){
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setObject(1, account.getId());
                pstmt.setString(2, account.getAccountNumber());
                pstmt.setBigDecimal(3, account.getBalance());
                pstmt.setString(4, account.getCurrency() != null ? account.getCurrency().name() : "MAD");
                pstmt.setString(5, String.valueOf(account.getType()));
                pstmt.setObject(6, client.getId());
                pstmt.setBoolean(7, account.isStatus());

                pstmt.execute();
            }catch(Exception e ){
                throw new RuntimeException("Failed Creation Account", e);
            }

        }

        public Account AccountByClient(Client client){
            String sql = "SELECT *, accounts.client_id, clients.full_name FROM accounts " +
                    "join clients on clients.id = accounts.client_id " +
                    "WHERE clients.id = ?";
            try(Connection conn = DatabaseConfig.getConnection()){
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setObject(1,client.getId());

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()){
                    Account account = new Account();
                    account.setId((UUID) rs.getObject("id"));
                    account.setAccountNumber(rs.getString("account_number"));
                    account.setType(Account.AccountType.valueOf(rs.getString("type")));
                    account.setBalance(rs.getBigDecimal("balance"));
                    return account;
                }



            }catch(Exception e){
                throw new RuntimeException(e);
            }
            return null;

        }

        @Override
        public List<Account> findByClientId(UUID clientId) {
            String sql = "SELECT id, account_number, balance, currency, type, client_id, status FROM accounts WHERE client_id = ?";
            List<Account> accounts = new ArrayList<>();
            try (Connection conn = DatabaseConfig.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setObject(1, clientId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Account a = new Account();
                    a.setId((UUID) rs.getObject("id"));
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setBalance(rs.getBigDecimal("balance"));
                    String currency = rs.getString("currency");
                    if (currency != null) {
                        a.setCurrency(Account.Currency.valueOf(currency));
                    }
                    a.setType(Account.AccountType.valueOf(rs.getString("type")));
                    a.setClient((UUID) rs.getObject("client_id"));
                    a.setStatus(rs.getBoolean("status"));
                    accounts.add(a);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching accounts by clientId", e);
            }
            return accounts;
        }
}

package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Account;
import com.demo1.Repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionRepositoryImplement implements TransactionRepository {

    @Override
    public void deposit(Account account, BigDecimal amount) {
        String updateAccountSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String insertTxSql = "INSERT INTO transactions (id, amount, currency, type, status, timestamp, account_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement up = conn.prepareStatement(updateAccountSql);
                 PreparedStatement ins = conn.prepareStatement(insertTxSql)) {

                // UPDATE account balance
                up.setBigDecimal(1, amount);
                up.setObject(2, account.getId());
                int rows = up.executeUpdate();
                if (rows != 1) throw new RuntimeException("Account not found or update failed");

                // INSERT transaction
                UUID txId = UUID.randomUUID();
                ins.setObject(1, txId);
                ins.setBigDecimal(2, amount);
                String curr = account.getCurrency() != null ? account.getCurrency().name() : "MAD";
                ins.setString(3, curr);
                ins.setString(4, "DEPOSIT");
                ins.setString(5, "SETTLED");
                ins.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ins.setObject(7, account.getId());
                ins.executeUpdate();

                conn.commit();
                conn.setAutoCommit(prevAuto);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error performing deposit", e);
        }
    }

    @Override
    public void withdraw(Account account, BigDecimal amount) {
        String updateAccountSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String insertTxSql = "INSERT INTO transactions (id, amount, currency, type, status, timestamp, account_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement up = conn.prepareStatement(updateAccountSql);
                 PreparedStatement ins = conn.prepareStatement(insertTxSql)) {

                // UPDATE account balance with sufficient funds guard
                up.setBigDecimal(1, amount);
                up.setObject(2, account.getId());
                up.setBigDecimal(3, amount);
                int rows = up.executeUpdate();
                if (rows != 1) throw new RuntimeException("Insufficient funds or account not found");

                // INSERT transaction
                UUID txId = UUID.randomUUID();
                ins.setObject(1, txId);
                ins.setBigDecimal(2, amount);
                String curr = account.getCurrency() != null ? account.getCurrency().name() : "MAD";
                ins.setString(3, curr);
                ins.setString(4, "WITHDRAW");
                ins.setString(5, "SETTLED");
                ins.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ins.setObject(7, account.getId());
                ins.executeUpdate();

                conn.commit();
                conn.setAutoCommit(prevAuto);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error performing withdraw", e);
        }
    }
}

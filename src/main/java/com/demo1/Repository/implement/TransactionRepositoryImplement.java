package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Account;
import com.demo1.Models.Transaction;
import com.demo1.Repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionRepositoryImplement implements TransactionRepository {

    @Override
    public void deposit(Account account, BigDecimal amount) {
        String updateAccountSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String insertTxSql = "INSERT INTO transactions (id, amount, currency, type, status, \"timestamp\", target_account_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement up = conn.prepareStatement(updateAccountSql);
                 PreparedStatement ins = conn.prepareStatement(insertTxSql)) {

                up.setBigDecimal(1, amount);
                up.setObject(2, account.getId());
                int rows = up.executeUpdate();
                if (rows != 1) throw new RuntimeException("Account not found or update failed");

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
    public void withdraw(Account account, BigDecimal amount, BigDecimal fee) {
        String updateAccountSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String insertTxSql = "INSERT INTO transactions (id, amount, currency, type, status, \"timestamp\", source_account_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement up = conn.prepareStatement(updateAccountSql);
                 PreparedStatement ins = conn.prepareStatement(insertTxSql)) {

                BigDecimal total = amount.add(fee);
                up.setBigDecimal(1, total);
                up.setObject(2, account.getId());
                up.setBigDecimal(3, total);
                int rows = up.executeUpdate();
                if (rows != 1) throw new RuntimeException("Insufficient funds or account not found");

                String curr = account.getCurrency() != null ? account.getCurrency().name() : "MAD";
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                UUID txId = UUID.randomUUID();
                ins.setObject(1, txId);
                ins.setBigDecimal(2, amount);
                ins.setString(3, curr);
                ins.setString(4, "WITHDRAW");
                ins.setString(5, "SETTLED");
                ins.setTimestamp(6, now);
                ins.setObject(7, account.getId());
                ins.executeUpdate();

                if (fee.compareTo(BigDecimal.ZERO) > 0) {
                    UUID feeId = UUID.randomUUID();
                    ins.setObject(1, feeId);
                    ins.setBigDecimal(2, fee);
                    ins.setString(3, curr);
                    ins.setString(4, "FEE");
                    ins.setString(5, "SETTLED");
                    ins.setTimestamp(6, now);
                    ins.setObject(7, account.getId());
                    ins.executeUpdate();
                }

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

    @Override
    public void transferInternal(Account from, Account to, BigDecimal amount) {
        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String insertTxSql = "INSERT INTO transactions(" +
                "id, amount, currency, type, status, \"timestamp\", source_account_id, target_account_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement debit = conn.prepareStatement(debitSql);
                 PreparedStatement credit = conn.prepareStatement(creditSql);
                 PreparedStatement ins = conn.prepareStatement(insertTxSql)) {

                debit.setBigDecimal(1, amount);
                debit.setObject(2, from.getId());
                debit.setBigDecimal(3, amount);
                int rowsDebit = debit.executeUpdate();
                if (rowsDebit != 1) throw new RuntimeException("Insufficient funds or source account not found");

                credit.setBigDecimal(1, amount);
                credit.setObject(2, to.getId());
                int rowsCredit = credit.executeUpdate();
                if (rowsCredit != 1) throw new RuntimeException("Destination account not found or update failed");

                String curr = from.getCurrency().name();
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                UUID txIdIn = UUID.randomUUID();
                ins.setObject(1, txIdIn);
                ins.setBigDecimal(2, amount);
                ins.setString(3, curr);
                ins.setString(4, "TRANSFER_IN");
                ins.setString(5, "SETTLED");
                ins.setTimestamp(6, now);
                ins.setObject(7, from.getId());
                ins.setObject(8, to.getId());
                ins.executeUpdate();

                conn.commit();
                conn.setAutoCommit(prevAuto);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error performing internal transfer", e);
        }
    }

    @Override
    public void transferOut(Account from, Account to, BigDecimal amount, BigDecimal fee) {
        String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String insertSql = "INSERT INTO transactions(id, amount, currency, type, status, \"timestamp\", source_account_id, target_account_id) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prev = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement debit = conn.prepareStatement(debitSql);
                 PreparedStatement credit = conn.prepareStatement(creditSql);
                 PreparedStatement ins = conn.prepareStatement(insertSql)) {

                BigDecimal total = amount.add(fee);
                debit.setBigDecimal(1, total);
                debit.setObject(2, from.getId());
                debit.setBigDecimal(3, total);
                int rd = debit.executeUpdate();
                if (rd != 1) throw new RuntimeException("Insufficient funds or source account not found");

                credit.setBigDecimal(1, amount);
                credit.setObject(2, to.getId());
                int rc = credit.executeUpdate();
                if (rc != 1) throw new RuntimeException("Destination account not found");

                String curr = from.getCurrency().name();
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                UUID outId = UUID.randomUUID();
                ins.setObject(1, outId);
                ins.setBigDecimal(2, amount);
                ins.setString(3, curr);
                ins.setString(4, "TRANSFER_OUT");
                ins.setString(5, "PENDING");
                ins.setTimestamp(6, now);
                ins.setObject(7, from.getId());
                ins.setObject(8, to.getId());
                ins.executeUpdate();

                if (fee.compareTo(BigDecimal.ZERO) > 0) {
                    UUID feeId = UUID.randomUUID();
                    ins.setObject(1, feeId);
                    ins.setBigDecimal(2, fee);
                    ins.setString(3, curr);
                    ins.setString(4, "FEE");
                    ins.setString(5, "SETTLED");
                    ins.setTimestamp(6, now);
                    ins.setObject(7, from.getId());
                    ins.setObject(8, null);
                    ins.executeUpdate();
                }

                conn.commit();
                conn.setAutoCommit(prev);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error performing transfer out", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT id, amount, currency, type, status, \"timestamp\", source_account_id, target_account_id FROM transactions ORDER BY \"timestamp\" DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId((UUID) rs.getObject("id"));
                t.setAmount(rs.getBigDecimal("amount"));
                String curr = rs.getString("currency");
                try { if (curr != null) t.setCurrency(Transaction.Currency.valueOf(curr)); } catch (Exception ignored) {}
                String type = rs.getString("type");
                try { if (type != null) t.setType(Transaction.TransactionType.valueOf(type)); } catch (Exception ignored) {}
                String st = rs.getString("status");
                try { if (st != null) t.setStatus(Transaction.TransactionStatus.valueOf(st)); } catch (Exception ignored) {}
                Timestamp ts = rs.getTimestamp("timestamp");
                if (ts != null) t.setTimestamp(ts.toLocalDateTime());
                // Only set account IDs in lightweight Account objects
                Object srcId = rs.getObject("source_account_id");
                if (srcId != null) {
                    Account src = new Account();
                    src.setId((UUID) srcId);
                    t.setSource_account_id(src);
                }
                Object tgtId = rs.getObject("target_account_id");
                if (tgtId != null) {
                    Account tgt = new Account();
                    tgt.setId((UUID) tgtId);
                    t.setTarget_account_id(tgt);
                }
                list.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading transactions", e);
        }
        return list;
    }
}

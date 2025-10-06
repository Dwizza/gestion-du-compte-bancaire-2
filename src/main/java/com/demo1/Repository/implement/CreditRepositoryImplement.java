package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.Account;
import com.demo1.Models.Credit;
import com.demo1.Repository.CreditRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditRepositoryImplement implements CreditRepository {

    @Override
    public Credit insertCredit(Credit credit, Account account) {
        String insertCredit = "INSERT INTO credits (id, amount, currency, interest_rate, start_date, end_date, credit_type, status, account_id) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ins = conn.prepareStatement(insertCredit)) {
            UUID id = UUID.randomUUID();
            credit.setId(id);
            ins.setObject(1, id);
            ins.setBigDecimal(2, credit.getAmount());
            ins.setString(3, credit.getCurrency().name());
            ins.setDouble(4, credit.getInterestRate());
            ins.setDate(5, Date.valueOf(credit.getStartDate()));
            ins.setDate(6, Date.valueOf(credit.getEndDate()));
            ins.setString(7, credit.getCreditType().name());
            ins.setString(8, credit.getStatus().name());
            ins.setObject(9, account.getId());
            System.out.println(credit);
            ins.executeUpdate();
            return credit;
        } catch (Exception e) {
            throw new RuntimeException("Error inserting credit", e);
        }
    }

    @Override
    public Credit approveCredit(UUID creditId) {
        String selectSql = "SELECT id, amount, status, account_id FROM credits WHERE id = ? FOR UPDATE";
        String updateAccount = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String updateCredit = "UPDATE credits SET status = 'ACTIVE' WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            boolean prev = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement sel = conn.prepareStatement(selectSql);
                 PreparedStatement updAcc = conn.prepareStatement(updateAccount);
                 PreparedStatement updCr = conn.prepareStatement(updateCredit)) {
                sel.setObject(1, creditId);
                BigDecimal amount = null;
                UUID accountId = null;
                String status = null;
                try (ResultSet rs = sel.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString("status");
                        if (status == null || !status.equalsIgnoreCase("PENDING")) {
                            throw new RuntimeException("Credit not in PENDING status");
                        }
                        amount = rs.getBigDecimal("amount");
                        accountId = (UUID) rs.getObject("account_id");
                    } else {
                        throw new RuntimeException("Credit not found");
                    }
                }
                updAcc.setBigDecimal(1, amount);
                updAcc.setObject(2, accountId);
                int ra = updAcc.executeUpdate();
                if (ra != 1) throw new RuntimeException("Account update failed");
                updCr.setObject(1, creditId);
                int rc = updCr.executeUpdate();
                if (rc != 1) throw new RuntimeException("Credit status update failed");
                conn.commit();
                conn.setAutoCommit(prev);
                return findById(creditId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error approving credit", e);
        }
    }

    @Override
    public List<Credit> findPending() {
        String sql = "SELECT c.id, c.amount, c.currency, c.interest_rate, c.start_date, c.end_date, c.credit_type, c.status, c.account_id, " +
                "a.account_number, a.type AS account_type, a.currency AS account_currency, a.balance AS account_balance, a.client_id " +
                "FROM credits c JOIN accounts a ON a.id = c.account_id WHERE c.status = 'PENDING' ORDER BY c.start_date";
        List<Credit> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCredit(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error listing pending credits", e);
        }
        return list;
    }

    @Override
    public Credit findById(UUID id) {
        String sql = "SELECT c.id, c.amount, c.currency, c.interest_rate, c.start_date, c.end_date, c.type, c.status, c.account_id, " +
                "a.account_number, a.type AS account_type, a.currency AS account_currency, a.balance AS account_balance, a.client_id " +
                "FROM credits c JOIN accounts a ON a.id = c.account_id WHERE c.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCredit(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit by id", e);
        }
        return null;
    }

    @Override
    public List<Credit> findByClient(UUID clientId) {
        String sql = "SELECT c.id, c.amount, c.currency, c.interest_rate, c.start_date, c.end_date, c.type, c.status, c.account_id, " +
                "a.account_number, a.type AS account_type, a.currency AS account_currency, a.balance AS account_balance, a.client_id " +
                "FROM credits c JOIN accounts a ON a.id = c.account_id WHERE a.client_id = ? ORDER BY c.start_date DESC";
        List<Credit> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCredit(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding credits by client", e);
        }
        return list;
    }

    @Override
    public List<Credit> findAll() {
        String sql = "SELECT c.id, c.amount, c.currency, c.interest_rate, c.start_date, c.end_date, c.type, c.status, c.account_id, " +
                "a.account_number, a.type AS account_type, a.currency AS account_currency, a.balance AS account_balance, a.client_id " +
                "FROM credits c JOIN accounts a ON a.id = c.account_id ORDER BY c.start_date DESC";
        List<Credit> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCredit(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error listing credits", e);
        }
        return list;
    }

    private Credit mapCredit(ResultSet rs) throws SQLException {
        Credit c = new Credit();
        c.setId((UUID) rs.getObject("id"));
        c.setAmount(rs.getBigDecimal("amount"));
        String curr = rs.getString("currency");

        if (curr != null) {
            try { c.setCurrency(Credit.Currency.valueOf(curr)); } catch (Exception ignored) {}
        }
        c.setInterestRate(rs.getDouble("interest_rate"));
        Date sd = rs.getDate("start_date");
        if (sd != null) c.setStartDate(sd.toLocalDate());
        Date ed = rs.getDate("end_date");
        if (ed != null) c.setEndDate(ed.toLocalDate());
        String type = rs.getString("credit_type");
        if (type != null) {
            try { c.setCreditType(Credit.CreditType.valueOf(type)); } catch (Exception ignored) {}
        }
        String status = rs.getString("status");
        if (status != null) {
            try { c.setStatus(Credit.CreditStatus.valueOf(status)); } catch (Exception ignored) {}
        }
        Account a = new Account();
        a.setId((UUID) rs.getObject("account_id"));
        a.setAccountNumber(rs.getString("account_number"));
        a.setBalance(rs.getBigDecimal("account_balance"));
        String accCurr = rs.getString("account_currency");
        if (accCurr != null) {
            try { a.setCurrency(Account.Currency.valueOf(accCurr)); } catch (Exception ignored) {}
        }
        String accType = rs.getString("account_type");
        if (accType != null) {
            try { a.setType(Account.AccountType.valueOf(accType)); } catch (Exception ignored) {}
        }
        a.setClient((UUID) rs.getObject("client_id"));
        c.setAccount(a);
        return c;
    }
}

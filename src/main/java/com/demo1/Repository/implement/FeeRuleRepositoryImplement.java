package com.demo1.Repository.implement;

import com.demo1.Config.DatabaseConfig;
import com.demo1.Models.FeeRule;
import com.demo1.Repository.FeeRuleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FeeRuleRepositoryImplement implements FeeRuleRepository {
    @Override
    public FeeRule findActiveByOperation(String operationType) {
        String sql = "SELECT id, operation_type, mode, value, currency, is_active FROM fees_rules WHERE operation_type = ? AND is_active = true LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, operationType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FeeRule r = new FeeRule();
                    r.setId(rs.getLong("id"));
                    r.setOperationType(rs.getString("operation_type"));
                    r.setMode(rs.getString("mode"));
                    r.setValue(rs.getBigDecimal("value"));
                    r.setCurrency(rs.getString("currency"));
                    r.setActive(rs.getBoolean("is_active"));
                    return r;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading fee rule", e);
        }
        return null;
    }
}


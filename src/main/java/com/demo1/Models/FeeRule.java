package com.demo1.Models;

import java.math.BigDecimal;

public class FeeRule {
    private Long id;              // id bigint
    private String operationType; // e.g. TRANSFER_OUT, WITHDRAW
    private String mode;          // PERCENT or FIX
    private BigDecimal value;     // percent or fixed amount
    private String currency;      // MAD
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}


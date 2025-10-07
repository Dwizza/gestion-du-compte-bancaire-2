package com.demo1.Repository;

import com.demo1.Models.FeeRule;

public interface FeeRuleRepository {
    FeeRule findActiveByOperation(String operationType);
}


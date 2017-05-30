package com.canopus.entity;

import com.canopus.mw.dto.*;

public interface AggregationRuleDataService
{
    public static final String ERR_AGGREGATION_RULE_INVALID_DATA = "agg-rule-err-001";
    public static final String ERR_AGGREGATION_RULE_DOES_NOT_EXIST = "agg-rule-err-002";
    public static final String ERR_AGGREGATION_RULE_UNKNOWN_EXCEPTION = "agg-rule-err-002";
    
    Response getAggregationRule(final Request p0);
    
    Response saveAggregationRule(final Request p0);
    
    Response deleteAggregationRule(final Request p0);
    
    Response getAllAggregationRules(final Request p0);
}

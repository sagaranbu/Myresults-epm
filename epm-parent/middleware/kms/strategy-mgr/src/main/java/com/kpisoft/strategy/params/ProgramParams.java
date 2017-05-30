package com.kpisoft.strategy.params;

import com.canopus.mw.dto.param.*;

public enum ProgramParams implements IMiddlewareParam
{
    STRATEGY_PROGRAM, 
    PROGRAM_POLICY_RULE, 
    STRATEGY_PROGRAM_LIST, 
    STATUS, 
    PROGRAM_ZONE_ID, 
    STRATEGY_PROGRAM_ID, 
    STRATEGY_PROGRAM_ID_LIST, 
    PROGRAM_POLICY_RULE_LIST, 
    PROGRAM_ZONE, 
    PERIOD_TYPE_ID, 
    EMP_ID, 
    POSITION_ID, 
    GRADE_ID, 
    ORG_ID, 
    MAP_RESULT, 
    NAME, 
    EXPRESSION, 
    WORKFLOW_LEVELS, 
    SUBMIT, 
    CASCADE_TYPE, 
    MODE_TYPE, 
    PROG_ID, 
    EXCLUSION_DATA, 
    EXCLUSION_DATA_LIST, 
    PROGRAM_ZONE_ID_LIST, 
    PROG_ID_LIST;
    
    public String getParamName() {
        return this.name();
    }
}

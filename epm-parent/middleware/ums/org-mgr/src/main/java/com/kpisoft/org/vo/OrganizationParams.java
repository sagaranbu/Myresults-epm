package com.kpisoft.org.vo;

import com.canopus.mw.dto.param.*;

public enum OrganizationParams implements IMiddlewareParam
{
    ORG_DATA, 
    ORG_IDENTIFIER, 
    ORG_DATA_LIST, 
    ORG_IDENTIFIER_LIST, 
    ORG_UNIT_NAME, 
    ORG_REL_DATA, 
    ORG_REL_ID, 
    ORG_REL_DATA_LIST, 
    CHILD_ORG_ID, 
    PARENT_ORG_ID, 
    DISTANCE, 
    STATUS, 
    PASSWORD_POLICY_DATA, 
    PASSWORD_POLICY_ID, 
    PASSWORD, 
    VALIDATION_MSG_LIST, 
    END_DATE, 
    DIMENSION_ID, 
    DELETE, 
    DELETE_CHILDS, 
    DELETE_EMPLOYEES, 
    SUSPEND_CHILDS, 
    SUSPEND_EMPLOYEES, 
    ORG_IDENTITY_REL_LIST, 
    ORG_GRAPH, 
    UPDATE_STATUS, 
    ORG_START_DATE, 
    ORG_END_DATE;
    
    public String getParamName() {
        return this.name();
    }
}

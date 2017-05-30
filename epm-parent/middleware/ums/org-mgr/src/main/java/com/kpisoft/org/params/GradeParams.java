package com.kpisoft.org.params;

import com.canopus.mw.dto.param.*;

public enum GradeParams implements IMiddlewareParam
{
    GRADE_DATA, 
    GRADE_DATA_LIST, 
    GRADE_ID, 
    SEARCH_FIRST_RESULT, 
    SEARCH_MAX_RESULT, 
    FIELD_TO_SORT, 
    GRD_COUNT, 
    ASC, 
    DESC, 
    ORDER_TYPE, 
    STATUS_RESPONSE;
    
    public String getParamName() {
        return this.name();
    }
}

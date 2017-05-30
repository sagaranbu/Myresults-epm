package com.kpisoft.org.params;

import com.canopus.mw.dto.param.*;

public enum EmpResolverParams implements IMiddlewareParam
{
    EMP_ID, 
    USER_ID, 
    USER_ID_LIST, 
    EXPRESSION;
    
    public String getParamName() {
        return this.name();
    }
}

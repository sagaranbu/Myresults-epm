package com.canopus.mw.dto.param;

public enum HeaderParam implements IMiddlewareParam
{
    TENANT_ID, 
    REQUEST_ID, 
    REQUEST_PATH, 
    AM_SESSION, 
    USER_DATA, 
    USER_LOCALE, 
    SYSTEM_LOCALE, 
    TENANT_LOCALE, 
    USER_ID, 
    PROXY_USER_ID, 
    USER_NAME, 
    PROXY_USER_NAME, 
    CROSS_TENANT, 
    EMPLOYEE_ID, 
    DESC, 
    ASC, 
    PAGE, 
    SORT, 
    SORT_LIST, 
    TEMP_ORG_ID, 
    TEMP_ORG_NAME;
    
    @Override
    public String getParamName() {
        return this.name();
    }
}

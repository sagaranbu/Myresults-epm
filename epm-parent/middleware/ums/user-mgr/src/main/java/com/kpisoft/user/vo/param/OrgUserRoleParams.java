package com.kpisoft.user.vo.param;

import com.canopus.mw.dto.param.*;

public enum OrgUserRoleParams implements IMiddlewareParam
{
    ORG_USER_ROLE_BEAN, 
    ORG_USER_ROLE_ID, 
    ORG_USER_ROLE_ID_LIST, 
    ORG_USER_ROLE_BEAN_LIST, 
    STATUS, 
    USER_ID_LIST, 
    ROLE_ID_LIST, 
    ORG_ID_LIST;
    
    public String getParamName() {
        return this.name();
    }
}

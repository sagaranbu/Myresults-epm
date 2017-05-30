package com.kpisoft.user.vo.param;

import com.canopus.mw.dto.param.*;

public enum OrgTypeRoleParams implements IMiddlewareParam
{
    ORG_TYPE_ROLE_BEAN, 
    ORG_TYPE_ROLE_ID, 
    ORG_TYPE_ROLE_ID_LIST, 
    ORG_TYPE_ROLE_BEAN_LIST, 
    STATUS, 
    ROLE_ID_LIST, 
    ORGTYPE_ID_LIST;
    
    public String getParamName() {
        return this.name();
    }
}

package com.kpisoft.org.params;

import com.canopus.mw.dto.param.*;

public enum OrgDimensionStructureParams implements IMiddlewareParam
{
    ORG_DIM_STR_DATA, 
    ORG_DIM_STR_ID, 
    ORG_STR_STATUS_RESPONSE, 
    ORG_STR_LIST;
    
    public String getParamName() {
        return this.name();
    }
}

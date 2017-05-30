package com.kpisoft.org.params;

import com.canopus.mw.dto.param.*;

public enum OrgDimensionParams implements IMiddlewareParam
{
    ORG_DIMENSION, 
    ORG_DIM_DATA, 
    ORG_DIM_LIST, 
    ORG_DIM_DATA_ID, 
    ORG_DIM_STATUS_RESPONSE, 
    ORG_STR_DATA;
    
    public String getParamName() {
        return this.name();
    }
}

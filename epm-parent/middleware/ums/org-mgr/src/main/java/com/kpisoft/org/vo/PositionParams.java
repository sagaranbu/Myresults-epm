package com.kpisoft.org.vo;

import com.canopus.mw.dto.param.*;

@Deprecated
public enum PositionParams implements IMiddlewareParam
{
    POSITION_DATA, 
    POSITION_DATA_LIST, 
    POSITION_IDENTIFIER, 
    POSITION_IDENTIFIER_LIST, 
    STATUS, 
    POSITION_REL_DATA, 
    POSITION_REL_DATA_LIST, 
    POSITION_REL_ID, 
    DISTANCE, 
    CHILD_POSITION_ID, 
    PARENT_POSITION_ID, 
    POS_IDENTITY_REL_DATA_LIST, 
    POSITION_GRAPH, 
    POSITIONS_COUNT;
    
    public String getParamName() {
        return this.name();
    }
}

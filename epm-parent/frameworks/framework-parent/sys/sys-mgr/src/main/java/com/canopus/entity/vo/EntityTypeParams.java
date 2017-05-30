package com.canopus.entity.vo;

import com.canopus.mw.dto.param.*;

public enum EntityTypeParams implements IMiddlewareParam
{
    ENTITY_FIELD_DATA, 
    ENTITY_FIELD_IDENTIFIER, 
    ENTITY_TYPE_IDENTIFIER, 
    ENTITY_FIELD_DATA_LIST, 
    STATUS, 
    LOCALE_NAME, 
    ENTITY_FIELD_LANG_DATA, 
    ENTITY_REL_LIST, 
    ENTITY_DATA, 
    ENTITY_REL_DATA, 
    ENTITY_DATA_LIST, 
    ENTITY_IDENTIFIER;
    
    public String getParamName() {
        return this.name();
    }
}

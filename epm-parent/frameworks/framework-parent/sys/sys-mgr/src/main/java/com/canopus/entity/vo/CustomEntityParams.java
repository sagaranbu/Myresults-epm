package com.canopus.entity.vo;

import com.canopus.mw.dto.param.*;

public enum CustomEntityParams implements IMiddlewareParam
{
    CUSTOM_ENTITY_ID, 
    CUSTOM_ENTITY_DATA, 
    CUSTOM_ENTITY_DATA_LIST, 
    CUSTOM_ENTITY_FIELDS_LIST, 
    FIELD_GROUP_ID, 
    DATA_TYPE_ID, 
    DATA_TYPE, 
    DATA_TYPE_LIST, 
    FIELD_TYPE_ID, 
    FIELD_TYPE, 
    FIELD_TYPE_LIST, 
    VALUE, 
    CUSTOM_ENTITY_INSTANCE_ID, 
    CUSTOM_ENTITY_INSTANCE_DATA, 
    CUSTOM_ENTITY_INSTANCE_LIST, 
    ENTITY_FIELD_LANG_DATA, 
    ENTITY_FIELD_LANG_DATA_LIST, 
    LOCALE, 
    CUSTOM_ENTITY_INSTANCE_FIELD_ID, 
    CUSTOM_ENTITY_INSTANCE_FIELD_DATA, 
    CUSTOM_ENTITY_INSTANCE_FIELD_LIST, 
    CUSTOM_ENTITY_FIELD_ID, 
    CUSTOM_ENTITY_FIELD_DATA, 
    CUSTOM_ENTITY_FIELD_STATUS;
    
    public String getParamName() {
        return this.name();
    }
}

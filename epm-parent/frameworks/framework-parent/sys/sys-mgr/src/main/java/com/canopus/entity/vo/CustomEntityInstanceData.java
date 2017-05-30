package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class CustomEntityInstanceData extends BaseValueObject
{
    private static final long serialVersionUID = -3440073057705830287L;
    private Integer id;
    private Integer entityId;
    private String name;
    private String eventHandler;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getEventHandler() {
        return this.eventHandler;
    }
    
    public void setEventHandler(final String eventHandler) {
        this.eventHandler = eventHandler;
    }
}

package com.canopus.mw.dto;

import java.util.*;

public class MiddlewareEventBean extends BaseValueObject
{
    private static final long serialVersionUID = 4383439926063569540L;
    private Integer id;
    private String eventType;
    private Boolean isProcessed;
    protected Map<String, Object> context;
    private Map<String, Object> variables;
    private Date scheduleDate;
    private byte[] eventData;
    
    public MiddlewareEventBean() {
        final Map<String, Object> currContext = ExecutionContext.getCurrent().getContextValues();
        this.context = ((currContext == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(currContext));
    }
    
    public Map<String, Object> getContext() {
        return this.context;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    public Boolean getIsProcessed() {
        return this.isProcessed;
    }
    
    public void setIsProcessed(final Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }
    
    public Date getScheduleDate() {
        return this.scheduleDate;
    }
    
    public void setScheduleDate(final Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    
    public void setEventData(final byte[] eventData) {
        this.eventData = eventData;
    }
    
    public byte[] getEventData() {
        return this.eventData;
    }
    
    public Map<String, Object> getVariables() {
        return this.variables;
    }
    
    public void setVariables(final Map<String, Object> variables) {
        this.variables = variables;
    }
}

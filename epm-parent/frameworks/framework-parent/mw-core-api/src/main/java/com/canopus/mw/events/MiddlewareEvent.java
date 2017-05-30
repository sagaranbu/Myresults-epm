package com.canopus.mw.events;

import com.canopus.mw.dto.*;
import java.util.*;

public class MiddlewareEvent extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> context;
    protected Map<String, BaseValueObject> payLoadMap;
    protected BaseValueObject payLoad;
    protected String eventType;
    protected Date scheduleDate;
    
    public MiddlewareEvent() {
        final Map<String, Object> currContext = ExecutionContext.getCurrent().getContextValues();
        this.context = ((currContext == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(currContext));
    }
    
    public Map<String, BaseValueObject> getPayLoadMap() {
        return this.payLoadMap;
    }
    
    public void setPayLoadMap(final Map<String, BaseValueObject> payLoadMap) {
        this.payLoadMap = payLoadMap;
    }
    
    public Map<String, Object> getContext() {
        return this.context;
    }
    
    public void setPayLoad(final String key, final BaseValueObject payLoad) {
        if (this.payLoadMap == null) {
            this.payLoadMap = new HashMap<String, BaseValueObject>();
        }
        this.payLoadMap.put(key, payLoad);
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    public Date getScheduleDate() {
        return this.scheduleDate;
    }
    
    public void setScheduleDate(final Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}

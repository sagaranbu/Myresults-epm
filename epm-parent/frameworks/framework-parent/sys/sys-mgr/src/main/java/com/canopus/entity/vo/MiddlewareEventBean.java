package com.canopus.entity.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class MiddlewareEventBean extends BaseValueObject
{
    private static final long serialVersionUID = 4383439926063569540L;
    private Integer id;
    private String eventType;
    private Boolean isProcessed;
    private Date scheduleDate;
    private byte[] eventData;
    
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
}

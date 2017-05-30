package com.canopus.interceptor.vo;

import java.util.*;

public class AuditLoggingData
{
    private Integer id;
    private Integer tenantId;
    private String requestId;
    private String requestState;
    private Date startTime;
    private Date endTime;
    
    public AuditLoggingData(final Integer id, final Integer tenantId, final String requestId, final String requestState, final Date startTime, final Date endTime) {
        this.id = id;
        this.tenantId = tenantId;
        this.requestId = requestId;
        this.requestState = requestState;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
    
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
    
    public String getRequestState() {
        return this.requestState;
    }
    
    public void setRequestState(final String requestState) {
        this.requestState = requestState;
    }
    
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
}

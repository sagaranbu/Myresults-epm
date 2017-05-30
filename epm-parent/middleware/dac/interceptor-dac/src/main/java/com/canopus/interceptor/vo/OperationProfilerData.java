package com.canopus.interceptor.vo;

import java.util.*;

public class OperationProfilerData
{
    private Integer id;
    private Integer tenantId;
    private String operationId;
    private String requestPath;
    private String requestId;
    private Date startTime;
    private Date endTime;
    private long executionTime;
    private String userId;
    private String proxyUserId;
    
    public OperationProfilerData(final Integer id, final Integer tenantId, final String operationId, final String requestPath, final String requestId, final Date startTime, final Date endTime, final long executionTime, final String userId, final String proxyUserId) {
        this.id = id;
        this.tenantId = tenantId;
        this.operationId = operationId;
        this.requestPath = requestPath;
        this.requestId = requestId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.executionTime = executionTime;
        this.userId = userId;
        this.proxyUserId = proxyUserId;
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
    
    public String getOperationId() {
        return this.operationId;
    }
    
    public void setOperationId(final String operationId) {
        this.operationId = operationId;
    }
    
    public String getRequestPath() {
        return this.requestPath;
    }
    
    public void setRequestPath(final String requestPath) {
        this.requestPath = requestPath;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
    
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
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
    
    public long getExecutionTime() {
        return this.executionTime;
    }
    
    public void setExecutionTime(final long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public String getProxyUserId() {
        return this.proxyUserId;
    }
    
    public void setProxyUserId(final String proxyUserId) {
        this.proxyUserId = proxyUserId;
    }
}

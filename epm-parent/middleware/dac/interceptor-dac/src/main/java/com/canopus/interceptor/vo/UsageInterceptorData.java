package com.canopus.interceptor.vo;

import java.util.*;

public class UsageInterceptorData
{
    private Integer id;
    private Integer tenantId;
    private String operationId;
    private Double mean;
    private Long min;
    private Long max;
    private Long count;
    private Date createdTime;
    
    public UsageInterceptorData(final Integer id, final Integer tenantId, final String operationId, final Double mean, final Long min, final Long max, final Long count, final Date createdTime) {
        this.id = id;
        this.tenantId = tenantId;
        this.operationId = operationId;
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.count = count;
        this.createdTime = createdTime;
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
    
    public Double getMean() {
        return this.mean;
    }
    
    public void setMean(final Double mean) {
        this.mean = mean;
    }
    
    public Long getMin() {
        return this.min;
    }
    
    public void setMin(final Long min) {
        this.min = min;
    }
    
    public Long getMax() {
        return this.max;
    }
    
    public void setMax(final Long max) {
        this.max = max;
    }
    
    public Long getCount() {
        return this.count;
    }
    
    public void setCount(final Long count) {
        this.count = count;
    }
    
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    public void setCreatedTime(final Date createdTime) {
        this.createdTime = createdTime;
    }
}

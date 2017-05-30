package com.canopus.interceptor.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "M1_USAGE_INTERCEPTOR")
public class UsageInterceptorEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MUI_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "MUI_ENTITY_ID_SEQ", sequenceName = "MUI_ENTITY_ID_SEQ")
    private Integer id;
    @Column(name = "TENANT_ID")
    private Integer tenantId;
    @Column(name = "OPERATION_ID", length = 500)
    private String operationId;
    @Column(name = "MEAN")
    private Double mean;
    @Column(name = "MIN")
    private Long min;
    @Column(name = "MAX")
    private Long max;
    @Column(name = "COUNT")
    private Long count;
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    
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

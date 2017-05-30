package com.canopus.interceptor.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "M1_OPR_PROF_INTCPTR")
public class OperationProfilerEntity extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 3153694639823155672L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MOPI_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "MOPI_ENTITY_ID_SEQ", sequenceName = "MOPI_ENTITY_ID_SEQ")
    @Column(name = "MOPI_PK_ID")
    private Integer id;
    @Column(name = "TENANT_ID")
    private Integer tenantId;
    @Column(name = "OPERATION_ID", length = 500)
    private String operationId;
    @Column(name = "REQUEST_PATH", length = 500)
    private String requestPath;
    @Column(name = "REQUEST_ID", length = 500)
    private String requestId;
    @Column(name = "START_TIME")
    private Date startTime;
    @Column(name = "END_TIME")
    private Date endTime;
    @Column(name = "EXECUTION_TIME")
    private Long executionTime;
    @Column(name = "USER_ID", length = 45)
    private String userId;
    @Column(name = "PROXY_USER_ID", length = 45)
    private String proxyUserId;
    
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
    
    public Long getExecutionTime() {
        return this.executionTime;
    }
    
    public void setExecutionTime(final Long executionTime) {
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

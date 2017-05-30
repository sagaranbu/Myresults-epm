package com.canopus.interceptor.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Audited
@Table(name = "M1_INTCPTR_REQUEST_STATS")
public class AuditLoggingEntity extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 6898558900656888470L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MRS_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "MRS_ENTITY_ID_SEQ", sequenceName = "MRS_ENTITY_ID_SEQ")
    @Column(name = "MRS_PK_ID")
    private Integer id;
    @Column(name = "TENANT_ID")
    private Integer tenantId;
    @Column(name = "REQUEST_ID", length = 500)
    private String requestId;
    @Column(name = "REQUEST_STATE", length = 127)
    private String requestState;
    @Column(name = "START_TIME")
    private Date startTime;
    @Column(name = "END_TIME")
    private Date endTime;
    
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

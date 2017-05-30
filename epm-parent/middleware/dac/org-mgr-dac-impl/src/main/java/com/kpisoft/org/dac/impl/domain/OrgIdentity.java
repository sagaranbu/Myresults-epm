package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Immutable
@Table(name = "ORG_DET_ORG_UNIT")
public class OrgIdentity extends BaseTenantEntity
{
    private static final long serialVersionUID = 2473015800361437837L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_ID_SEQ")
    @SequenceGenerator(name = "ORG_ID_SEQ", sequenceName = "ORG_ID_SEQ")
    @Column(name = "ODO_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ODO_AT_ORG_TYPE", length = 11)
    private Integer orgType;
    @Column(name = "ODO_AT_LEVEL", length = 11)
    private Integer level;
    @Column(name = "ODO_AT_NAME", length = 512, nullable = false)
    private String orgName;
    @Column(name = "ORGUNIT_CODE", length = 45, nullable = false)
    private String orgUnitCode;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    
    public OrgIdentity() {
        this.startDate = new Date();
        this.status = 1;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgType() {
        return this.orgType;
    }
    
    public void setOrgType(final Integer orgType) {
        this.orgType = orgType;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }
    
    public String getOrgUnitCode() {
        return this.orgUnitCode;
    }
    
    public void setOrgUnitCode(final String orgUnitCode) {
        this.orgUnitCode = orgUnitCode;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
}

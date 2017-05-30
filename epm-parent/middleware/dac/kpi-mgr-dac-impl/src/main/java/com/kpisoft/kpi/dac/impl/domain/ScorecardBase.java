package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "STR_DET_EMP_SCORECARD")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ScorecardBase extends BaseTenantEntity
{
    private static final long serialVersionUID = 8222136529213076359L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCORECARD_SEQ")
    @SequenceGenerator(name = "SCORECARD_SEQ", sequenceName = "SCORECARD_SEQ", allocationSize = 100)
    @Column(name = "SDE_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EDE_FK_ID", length = 11)
    private Integer employeeId;
    @Column(name = "SDP_FK_ID", length = 11)
    private Integer strDetailProgram;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "STATE", length = 11)
    private Integer state;
    @Column(name = "REVIEWED_BY_ID", length = 11)
    private Integer reviewedBy;
    @Column(name = "VERIFIED_BY_ID", length = 11)
    private Integer verifiedBy;
    @Column(name = "ODO_ORGANIZATION_FK_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "OMP_POSITION_FK_ID", length = 11)
    private Integer positionId;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getStrDetailProgram() {
        return this.strDetailProgram;
    }
    
    public void setStrDetailProgram(final Integer strDetailProgram) {
        this.strDetailProgram = strDetailProgram;
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
    
    public Integer getState() {
        return this.state;
    }
    
    public void setState(final Integer state) {
        this.state = state;
    }
    
    public Integer getReviewedBy() {
        return this.reviewedBy;
    }
    
    public void setReviewedBy(final Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public Integer getVerifiedBy() {
        return this.verifiedBy;
    }
    
    public void setVerifiedBy(final Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

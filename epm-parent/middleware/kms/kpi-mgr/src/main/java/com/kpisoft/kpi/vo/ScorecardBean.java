package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class ScorecardBean extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer employeeId;
    private Integer strDetailProgram;
    private Date startDate;
    private Date endDate;
    private Integer state;
    private Integer reviewedBy;
    private Integer verifiedBy;
    private Integer orgUnitId;
    private Integer positionId;
    private List<KpiScorecardRelationshipBean> kpiScorecardRelationships;
    
    public ScorecardBean() {
        this.kpiScorecardRelationships = new ArrayList<KpiScorecardRelationshipBean>();
    }
    
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
    
    public List<KpiScorecardRelationshipBean> getKpiScorecardRelationships() {
        return this.kpiScorecardRelationships;
    }
    
    public void setKpiScorecardRelationships(final List<KpiScorecardRelationshipBean> kpiScorecardRelationships) {
        this.kpiScorecardRelationships = kpiScorecardRelationships;
    }
}

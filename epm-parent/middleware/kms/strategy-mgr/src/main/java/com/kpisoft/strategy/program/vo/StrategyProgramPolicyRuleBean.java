package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class StrategyProgramPolicyRuleBean extends BaseValueObject
{
    private static final long serialVersionUID = -5984351192145738904L;
    private Integer id;
    private String value;
    private Integer orgUnitId;
    private Integer kpiId;
    private Integer employeeGradeId;
    private Integer orgPositionId;
    private Date startDate;
    private Date endDate;
    private Integer employeeId;
    private Integer systemBaseId;
    private Integer programId;
    private List<KpiRuleBean> kpiRuleConfigMetaData;
    
    public StrategyProgramPolicyRuleBean() {
        this.kpiRuleConfigMetaData = new ArrayList<KpiRuleBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public Integer getEmployeeGradeId() {
        return this.employeeGradeId;
    }
    
    public void setEmployeeGradeId(final Integer employeeGradeId) {
        this.employeeGradeId = employeeGradeId;
    }
    
    public Integer getOrgPositionId() {
        return this.orgPositionId;
    }
    
    public void setOrgPositionId(final Integer orgPositionId) {
        this.orgPositionId = orgPositionId;
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
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getSystemBaseId() {
        return this.systemBaseId;
    }
    
    public void setSystemBaseId(final Integer systemBaseId) {
        this.systemBaseId = systemBaseId;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
    
    public List<KpiRuleBean> getKpiRuleConfigMetaData() {
        return this.kpiRuleConfigMetaData;
    }
    
    public void setKpiRuleConfigMetaData(final List<KpiRuleBean> kpiRuleConfigMetaData) {
        this.kpiRuleConfigMetaData = kpiRuleConfigMetaData;
    }
}

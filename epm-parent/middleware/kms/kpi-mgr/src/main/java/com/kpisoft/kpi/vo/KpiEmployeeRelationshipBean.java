package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiEmployeeRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = 6764505275400721581L;
    private Integer id;
    private Integer relationshipBaseId;
    private Integer employeeId;
    private Integer kpiId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getRelationshipBaseId() {
        return this.relationshipBaseId;
    }
    
    public void setRelationshipBaseId(final Integer relationshipBaseId) {
        this.relationshipBaseId = relationshipBaseId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
}

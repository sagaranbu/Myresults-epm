package com.kpisoft.kpi.vo;

import com.canopus.dac.*;

public class KpiScorecardRelationshipBean extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -4327297801596532650L;
    private Integer id;
    private Integer empScorecardId;
    private Integer kpiId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEmpScorecardId() {
        return this.empScorecardId;
    }
    
    public void setEmpScorecardId(final Integer empScorecardId) {
        this.empScorecardId = empScorecardId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
}

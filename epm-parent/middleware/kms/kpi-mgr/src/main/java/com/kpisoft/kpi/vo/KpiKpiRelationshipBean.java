package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiKpiRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = -2767244377891215025L;
    private Integer id;
    private Integer kpiSourceId;
    private Integer SysEntityRelation;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getKpiSourceId() {
        return this.kpiSourceId;
    }
    
    public void setKpiSourceId(final Integer kpiSourceId) {
        this.kpiSourceId = kpiSourceId;
    }
    
    public Integer getSysEntityRelation() {
        return this.SysEntityRelation;
    }
    
    public void setSysEntityRelation(final Integer sysEntityRelation) {
        this.SysEntityRelation = sysEntityRelation;
    }
}

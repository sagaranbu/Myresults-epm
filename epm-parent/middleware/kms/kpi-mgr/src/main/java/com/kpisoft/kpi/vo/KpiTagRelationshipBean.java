package com.kpisoft.kpi.vo;

import com.canopus.dac.*;

public class KpiTagRelationshipBean extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 5809751374005090071L;
    private Integer id;
    private Integer kpiTagId;
    private Integer kpiId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getKpiTagId() {
        return this.kpiTagId;
    }
    
    public void setKpiTagId(final Integer kpiTagId) {
        this.kpiTagId = kpiTagId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
}

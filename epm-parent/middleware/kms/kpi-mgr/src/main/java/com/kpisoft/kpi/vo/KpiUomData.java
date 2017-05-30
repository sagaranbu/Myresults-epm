package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiUomData extends BaseValueObject
{
    private static final long serialVersionUID = -7623975818039052291L;
    private Integer id;
    private Integer version;
    private Integer ouTenantId;
    private Integer displayOrderId;
    private KpiAggregationTypeBean aggrType;
    private String unit;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getOuTenantId() {
        return this.ouTenantId;
    }
    
    public void setOuTenantId(final Integer ouTenantId) {
        this.ouTenantId = ouTenantId;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(final String unit) {
        this.unit = unit;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public KpiAggregationTypeBean getAggrType() {
        return this.aggrType;
    }
    
    public void setAggrType(final KpiAggregationTypeBean aggrType) {
        this.aggrType = aggrType;
    }
}

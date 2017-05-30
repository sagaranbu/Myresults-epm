package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiAggregationTypeBean extends BaseValueObject
{
    private static final long serialVersionUID = 569377553094113041L;
    private Integer id;
    private String type;
    private Integer displayOrderId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

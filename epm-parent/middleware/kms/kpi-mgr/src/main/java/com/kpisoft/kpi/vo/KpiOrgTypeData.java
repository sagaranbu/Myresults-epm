package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiOrgTypeData extends BaseValueObject
{
    private static final long serialVersionUID = -6926632053988974556L;
    private Integer id;
    private Integer type;
    private Integer version;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
}

package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiCategoryData extends BaseValueObject
{
    private static final long serialVersionUID = 3867140253812284497L;
    private Integer id;
    private Integer categoryGroup;
    private Integer version;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getCategoryGroup() {
        return this.categoryGroup;
    }
    
    public void setCategoryGroup(final Integer categoryGroup) {
        this.categoryGroup = categoryGroup;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
}

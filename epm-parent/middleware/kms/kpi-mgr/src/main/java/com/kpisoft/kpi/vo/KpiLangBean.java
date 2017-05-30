package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiLangBean extends BaseValueObject
{
    private static final long serialVersionUID = -7739917948162211487L;
    private Integer id;
    private KpiData kpiData;
    private String locale;
    private String name;
    private String description;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public KpiData getKpi() {
        return this.kpiData;
    }
    
    public void setKpi(final KpiData kpiData) {
        this.kpiData = kpiData;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}

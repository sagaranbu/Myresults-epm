package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class PeriodMasterLangBean extends BaseValueObject
{
    private static final long serialVersionUID = 6720326921719199033L;
    private Integer id;
    private String lacal;
    private String name;
    private PeriodMasterBean periodMaster;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getLacal() {
        return this.lacal;
    }
    
    public void setLacal(final String lacal) {
        this.lacal = lacal;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public PeriodMasterBean getPeriodMaster() {
        return this.periodMaster;
    }
    
    public void setPeriodMaster(final PeriodMasterBean periodMaster) {
        this.periodMaster = periodMaster;
    }
}

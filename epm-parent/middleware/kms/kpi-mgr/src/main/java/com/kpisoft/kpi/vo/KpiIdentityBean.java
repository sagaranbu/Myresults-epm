package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiIdentityBean extends BaseValueObject
{
    private static final long serialVersionUID = -2590747303206205742L;
    private Integer id;
    private String name;
    private Float weightage;
    private String code;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public boolean equals(final Object obj) {
        final KpiIdentityBean bean = (KpiIdentityBean)obj;
        return bean.getId() == this.id;
    }
    
    public int hashCode() {
        return 17;
    }
}

package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class KpiKpiGraphRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = -9100040674649549921L;
    private Integer id;
    @NotNull
    private KpiIdentityBean parent;
    private KpiIdentityBean child;
    @NotNull
    private String type;
    private Float weightage;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public KpiIdentityBean getParent() {
        return this.parent;
    }
    
    public void setParent(final KpiIdentityBean parent) {
        this.parent = parent;
    }
    
    public KpiIdentityBean getChild() {
        return this.child;
    }
    
    public void setChild(final KpiIdentityBean child) {
        this.child = child;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
}

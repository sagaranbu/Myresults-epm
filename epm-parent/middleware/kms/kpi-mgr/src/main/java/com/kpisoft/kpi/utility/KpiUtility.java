package com.kpisoft.kpi.utility;

import com.canopus.mw.dto.*;

public class KpiUtility extends BaseValueObject
{
    private static final long serialVersionUID = -3685184404772395482L;
    private Integer id;
    private float weightage;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final float weightage) {
        this.weightage = weightage;
    }
}

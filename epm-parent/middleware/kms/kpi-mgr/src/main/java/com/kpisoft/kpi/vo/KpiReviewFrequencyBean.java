package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiReviewFrequencyBean extends BaseValueObject
{
    private static final long serialVersionUID = 2334434322547586861L;
    private Integer id;
    private PeriodTypeBean periodType;
    private Integer orgTenantId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PeriodTypeBean getPeriodType() {
        return this.periodType;
    }
    
    public void setPeriodType(final PeriodTypeBean periodType) {
        this.periodType = periodType;
    }
    
    public Integer getOrgTenantId() {
        return this.orgTenantId;
    }
    
    public void setOrgTenantId(final Integer orgTenantId) {
        this.orgTenantId = orgTenantId;
    }
}

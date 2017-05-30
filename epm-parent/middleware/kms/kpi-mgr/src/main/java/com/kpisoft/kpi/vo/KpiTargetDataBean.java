package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiTargetDataBean extends BaseValueObject implements Comparable<KpiTargetDataBean>
{
    private static final long serialVersionUID = 8752961973543137081L;
    private Integer id;
    private Double targetDataNum;
    private Integer periodMasterId;
    private Integer kpimetTargetId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getTargetDataNum() {
        return this.targetDataNum;
    }
    
    public void setTargetDataNum(final Double targetDataNum) {
        this.targetDataNum = targetDataNum;
    }
    
    public Integer getPeriodMasterId() {
        return this.periodMasterId;
    }
    
    public void setPeriodMasterId(final Integer periodMasterId) {
        this.periodMasterId = periodMasterId;
    }
    
    public Integer getKpimetTargetId() {
        return this.kpimetTargetId;
    }
    
    public void setKpimetTargetId(final Integer kpimetTargetId) {
        this.kpimetTargetId = kpimetTargetId;
    }
    
    public int compareTo(final KpiTargetDataBean t) {
        if (this.getPeriodMasterId() != null && t.getPeriodMasterId() != null) {
            return this.getPeriodMasterId() - t.getPeriodMasterId();
        }
        if (this.getPeriodMasterId() != null) {
            return this.getPeriodMasterId();
        }
        if (t.getPeriodMasterId() != null) {
            return t.getPeriodMasterId();
        }
        return 0;
    }
}

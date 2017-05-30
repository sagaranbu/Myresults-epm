package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;
import com.canopus.entity.vo.*;

public class PeriodMasterBean extends BaseValueObject
{
    private static final long serialVersionUID = 5647172247695577267L;
    private Integer id;
    private PeriodTypeBean periodtype;
    private Date startDate;
    private Date endDate;
    private SystemMasterBaseBean systemMasterBase;
    private Integer displayOrderId;
    private String applicablePeriodIds;
    private Integer endPeriodMasterId;
    
    public String getApplicablePeriodIds() {
        return this.applicablePeriodIds;
    }
    
    public void setApplicablePeriodIds(final String applicablePeriodIds) {
        this.applicablePeriodIds = applicablePeriodIds;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PeriodTypeBean getPeriodtype() {
        return this.periodtype;
    }
    
    public void setPeriodtype(final PeriodTypeBean periodtype) {
        this.periodtype = periodtype;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
    
    public SystemMasterBaseBean getSystemMasterBase() {
        return this.systemMasterBase;
    }
    
    public void setSystemMasterBase(final SystemMasterBaseBean systemMasterBase) {
        this.systemMasterBase = systemMasterBase;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public Integer getEndPeriodMasterId() {
        return this.endPeriodMasterId;
    }
    
    public void setEndPeriodMasterId(final Integer endPeriodMasterId) {
        this.endPeriodMasterId = endPeriodMasterId;
    }
}

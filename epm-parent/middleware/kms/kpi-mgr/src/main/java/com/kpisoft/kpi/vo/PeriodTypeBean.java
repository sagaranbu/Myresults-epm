package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;

public class PeriodTypeBean extends BaseValueObject
{
    private static final long serialVersionUID = 6554939949230597508L;
    private Integer id;
    private SystemMasterBaseBean systemMasterBase;
    private Integer displayOrderId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
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
}

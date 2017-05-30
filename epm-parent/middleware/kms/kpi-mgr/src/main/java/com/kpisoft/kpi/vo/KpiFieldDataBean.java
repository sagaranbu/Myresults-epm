package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiFieldDataBean extends BaseValueObject
{
    private static final long serialVersionUID = -9103390434430212510L;
    private Integer id;
    private Integer sysMetFieldId;
    private Integer status;
    private String data;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getSysMetFieldId() {
        return this.sysMetFieldId;
    }
    
    public void setSysMetFieldId(final Integer sysMetFieldId) {
        this.sysMetFieldId = sysMetFieldId;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
}

package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class OrganizationFiledData extends BaseValueObject
{
    private static final long serialVersionUID = 1426701918469471501L;
    private Integer id;
    private Integer status;
    private Integer orgUnitId;
    @NotNull
    private Integer fieldId;
    private String data;
    
    public OrganizationFiledData() {
        this.status = 1;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getFieldId() {
        return this.fieldId;
    }
    
    public void setFieldId(final Integer fieldId) {
        this.fieldId = fieldId;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
}

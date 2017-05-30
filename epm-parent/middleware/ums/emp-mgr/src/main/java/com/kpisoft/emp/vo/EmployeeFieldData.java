package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;

public class EmployeeFieldData extends BaseValueObject
{
    private static final long serialVersionUID = -3105506838405526445L;
    private Integer id;
    private Integer fieldId;
    private Integer employeeId;
    private String data;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getFieldId() {
        return this.fieldId;
    }
    
    public void setFieldId(final Integer fieldId) {
        this.fieldId = fieldId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
}

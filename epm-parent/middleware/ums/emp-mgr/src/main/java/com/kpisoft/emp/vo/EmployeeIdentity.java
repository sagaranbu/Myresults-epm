package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class EmployeeIdentity extends BaseValueObject
{
    private static final long serialVersionUID = 701548031964074199L;
    private Integer id;
    @NotNull
    private String empCode;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer employeeId) {
        this.id = employeeId;
    }
    
    public String getEmpCode() {
        return this.empCode;
    }
    
    public void setEmpCode(final String empCode) {
        this.empCode = empCode;
    }
}

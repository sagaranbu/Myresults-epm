package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class EmployeeSupervisorRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = 8173115517722092176L;
    private Integer id;
    @NotNull
    private Integer employeeId;
    @NotNull
    private Integer supervisorId;
    @NotNull
    private Boolean primary;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public EmployeeSupervisorRelationshipData() {
        this.primary = false;
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getSupervisorId() {
        return this.supervisorId;
    }
    
    public void setSupervisorId(final Integer supervisorId) {
        this.supervisorId = supervisorId;
    }
    
    public Boolean getPrimary() {
        return this.primary;
    }
    
    public void setPrimary(final Boolean primary) {
        this.primary = primary;
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
}

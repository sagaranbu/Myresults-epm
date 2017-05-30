package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class EmployeePositionData extends BaseValueObject
{
    private static final long serialVersionUID = 2392131177490737356L;
    private Integer id;
    private Integer positionType;
    @NotNull
    private Integer positionId;
    @NotNull
    private Integer employeeId;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public EmployeePositionData() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getPositionType() {
        return this.positionType;
    }
    
    public void setPositionType(final Integer positionType) {
        this.positionType = positionType;
    }
    
    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
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

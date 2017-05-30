package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class EmployeeOrgRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = -7201150392886892968L;
    private Integer id;
    @NotNull
    private Integer organizationId;
    @NotNull
    private Integer employeeId;
    private Integer type;
    @NotNull
    private Boolean hod;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public EmployeeOrgRelationshipData() {
        this.hod = false;
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrganizationId() {
        return this.organizationId;
    }
    
    public void setOrganizationId(final Integer organizationId) {
        this.organizationId = organizationId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public Boolean getHod() {
        return this.hod;
    }
    
    public void setHod(final Boolean hod) {
        this.hod = hod;
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

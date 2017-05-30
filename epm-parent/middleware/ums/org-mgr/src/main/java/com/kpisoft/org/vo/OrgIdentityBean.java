package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class OrgIdentityBean extends BaseValueObject
{
    private static final long serialVersionUID = 1521267509070667657L;
    private Integer id;
    private Integer level;
    private Integer orgType;
    private Integer status;
    @NotNull
    private String orgName;
    @NotNull
    private String orgUnitCode;
    @NotNull
    private Date startDate;
    private Date endDate;
    private Date createDate;
    
    public OrgIdentityBean() {
        this.status = 1;
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgType() {
        return this.orgType;
    }
    
    public void setOrgType(final Integer orgType) {
        this.orgType = orgType;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }
    
    public String getOrgUnitCode() {
        return this.orgUnitCode;
    }
    
    public void setOrgUnitCode(final String orgUnitCode) {
        this.orgUnitCode = orgUnitCode;
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
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }
}

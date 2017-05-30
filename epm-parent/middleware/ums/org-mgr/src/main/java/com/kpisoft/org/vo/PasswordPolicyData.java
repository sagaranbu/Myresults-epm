package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class PasswordPolicyData extends BaseValueObject
{
    private static final long serialVersionUID = -2887868298006625536L;
    private Integer id;
    private Integer orgId;
    private Integer minAge;
    private Integer maxAge;
    private Integer minLength;
    private Integer maxLength;
    private Integer minCaps;
    private Integer minNumbers;
    private Integer minSpecial;
    private Integer maxFailureAttempts;
    private Integer lockoutDuration;
    private Integer isDefault;
    private Date startDate;
    private Date endDate;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgId() {
        return this.orgId;
    }
    
    public void setOrgId(final Integer orgId) {
        this.orgId = orgId;
    }
    
    public Integer getMinAge() {
        return this.minAge;
    }
    
    public void setMinAge(final Integer minAge) {
        this.minAge = minAge;
    }
    
    public Integer getMaxAge() {
        return this.maxAge;
    }
    
    public void setMaxAge(final Integer maxAge) {
        this.maxAge = maxAge;
    }
    
    public Integer getMinLength() {
        return this.minLength;
    }
    
    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }
    
    public Integer getMaxLength() {
        return this.maxLength;
    }
    
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }
    
    public Integer getMinCaps() {
        return this.minCaps;
    }
    
    public void setMinCaps(final Integer minCaps) {
        this.minCaps = minCaps;
    }
    
    public Integer getMinNumbers() {
        return this.minNumbers;
    }
    
    public void setMinNumbers(final Integer minNumbers) {
        this.minNumbers = minNumbers;
    }
    
    public Integer getMinSpecial() {
        return this.minSpecial;
    }
    
    public void setMinSpecial(final Integer minSpecial) {
        this.minSpecial = minSpecial;
    }
    
    public Integer getMaxFailureAttempts() {
        return this.maxFailureAttempts;
    }
    
    public void setMaxFailureAttempts(final Integer maxFailureAttempts) {
        this.maxFailureAttempts = maxFailureAttempts;
    }
    
    public Integer getLockoutDuration() {
        return this.lockoutDuration;
    }
    
    public void setLockoutDuration(final Integer lockoutDuration) {
        this.lockoutDuration = lockoutDuration;
    }
    
    public Integer getIsDefault() {
        return this.isDefault;
    }
    
    public void setIsDefault(final Integer isDefault) {
        this.isDefault = isDefault;
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

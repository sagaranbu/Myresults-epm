package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ORG_RUL_PASSWORD_POLICY")
@SQLDelete(sql = "UPDATE ORG_RUL_PASSWORD_POLICY SET IS_DELETED = 1 WHERE ORP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PasswordPolicy extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 2549354197085077979L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_PASSWORD_POLICY_ID_SEQ")
    @SequenceGenerator(name = "ORG_PASSWORD_POLICY_ID_SEQ", sequenceName = "ORG_PASSWORD_POLICY_ID_SEQ")
    @Column(name = "ORP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "MIN_AGE", length = 11)
    private Integer minAge;
    @Column(name = "MAX_AGE", length = 11)
    private Integer maxAge;
    @Column(name = "MIN_LENGTH", length = 11)
    private Integer minLength;
    @Column(name = "MAX_LENGTH", length = 11)
    private Integer maxLength;
    @Column(name = "MIN_CAPS", length = 11)
    private Integer minCaps;
    @Column(name = "MIN_NUMBERS", length = 11)
    private Integer minNumbers;
    @Column(name = "MIN_SPECIAL", length = 11)
    private Integer minSpecial;
    @Column(name = "MAX_FAILURE_ATTEMPTS", length = 11)
    private Integer maxFailureAttempts;
    @Column(name = "LOCKOUT_DURATION", length = 11)
    private Integer lockoutDuration;
    @Column(name = "IS_DEFAULT", length = 11)
    private Integer isDefault;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "ODO_FK_ID", length = 11)
    private Integer orgId;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
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
    
    public Integer getOrgId() {
        return this.orgId;
    }
    
    public void setOrgId(final Integer orgId) {
        this.orgId = orgId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

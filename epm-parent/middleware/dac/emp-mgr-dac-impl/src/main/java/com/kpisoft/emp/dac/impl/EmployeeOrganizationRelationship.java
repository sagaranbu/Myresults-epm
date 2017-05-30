package com.kpisoft.emp.dac.impl;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_REL_EMP_ORG_REL")
@SQLDelete(sql = "UPDATE ORG_REL_EMP_ORG_REL SET IS_DELETED = 1 WHERE ORE_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EmployeeOrganizationRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 4487282725193182585L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_ORG_ID_SEQ")
    @SequenceGenerator(name = "EMP_ORG_ID_SEQ", sequenceName = "EMP_ORG_ID_SEQ")
    @Column(name = "ORE_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ODO_FK_ID", length = 11, nullable = false)
    private Integer organizationId;
    @Column(name = "EDE_PK_EMPLOYEE_ID", length = 11)
    private Integer employeeId;
    @Column(name = "TYPE", length = 11)
    private Integer type;
    @Column(name = "IS_HOD", length = 11, nullable = false)
    private Boolean hod;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    public EmployeeOrganizationRelationship() {
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
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

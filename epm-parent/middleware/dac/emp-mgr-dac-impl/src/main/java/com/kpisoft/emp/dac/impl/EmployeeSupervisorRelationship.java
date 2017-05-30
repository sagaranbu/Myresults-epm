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
@Table(name = "EMP_REL_EMP_SUPERVISOR", uniqueConstraints = { @UniqueConstraint(columnNames = { "EDE_PK_EMPLOYEE_ID", "EDE_AT_SUPID" }) })
@SQLDelete(sql = "UPDATE EMP_REL_EMP_SUPERVISOR SET IS_DELETED = 1 WHERE EDE_PK_EMP_REL_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EmployeeSupervisorRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 4771677946651978637L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_SUP_ID_SEQ")
    @SequenceGenerator(name = "EMP_SUP_ID_SEQ", sequenceName = "EMP_SUP_ID_SEQ")
    @Column(name = "EDE_PK_EMP_REL_ID", length = 11)
    private Integer id;
    @Column(name = "EDE_PK_EMPLOYEE_ID", length = 11)
    private Integer employeeId;
    @Column(name = "EDE_AT_SUPID", length = 11, nullable = false)
    private Integer supervisorId;
    @Column(name = "EDE_IS_PRIMARY", length = 11, nullable = false)
    private Boolean primary;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    public EmployeeSupervisorRelationship() {
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

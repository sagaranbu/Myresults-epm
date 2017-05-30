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
@Table(name = "EMP_REL_POS_ASSIGNED")
@SQLDelete(sql = "UPDATE EMP_REL_POS_ASSIGNED SET IS_DELETED = 1 WHERE ERP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EmployeePositionRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 5650420633874702035L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_POS_ID_SEQ")
    @SequenceGenerator(name = "EMP_POS_ID_SEQ", sequenceName = "EMP_POS_ID_SEQ")
    @Column(name = "ERP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "POSITION_TYPE", length = 11)
    private Integer positionType;
    @Column(name = "OMP_FK_ID", length = 11, nullable = false)
    private Integer positionId;
    @Column(name = "EDE_FK_EMPLOYEE_ID", length = 11)
    private Integer employeeId;
    @Column(name = "START_DATE", length = 45, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public EmployeePositionRelationship() {
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

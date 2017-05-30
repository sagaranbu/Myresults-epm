package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "STR_RUL_EXCLUSIONS")
@SQLDelete(sql = "UPDATE STR_RUL_EXCLUSIONS SET IS_DELETED = 1 WHERE KRE_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ExclusionsMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = 7454332719372255793L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KRE_ID_SEQ")
    @SequenceGenerator(name = "KRE_ID_SEQ", sequenceName = "KRE_ID_SEQ")
    @Column(name = "KRE_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EMG_FK_ID", length = 11)
    private Integer employeeGradeId;
    @Column(name = "OMP_FK_ID", length = 11)
    private Integer orgPositionId;
    @Column(name = "ODO_FK_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "EDE_FK_ID", length = 11)
    private Integer employeeId;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "SDP_FK_ID", length = 11)
    private Integer programId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEmployeeGradeId() {
        return this.employeeGradeId;
    }
    
    public void setEmployeeGradeId(final Integer employeeGradeId) {
        this.employeeGradeId = employeeGradeId;
    }
    
    public Integer getOrgPositionId() {
        return this.orgPositionId;
    }
    
    public void setOrgPositionId(final Integer orgPositionId) {
        this.orgPositionId = orgPositionId;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
}

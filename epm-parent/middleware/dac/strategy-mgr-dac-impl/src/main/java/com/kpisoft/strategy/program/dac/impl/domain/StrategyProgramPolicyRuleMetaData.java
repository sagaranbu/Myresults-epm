package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import org.hibernate.envers.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "STR_RUL_PROG_POLICY")
@SQLDelete(sql = "UPDATE STR_RUL_PROG_POLICY SET IS_DELETED = 1 WHERE SRP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyProgramPolicyRuleMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = 819185430300187168L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PROG_ID_SEQ")
    @SequenceGenerator(name = "PROG_ID_SEQ", sequenceName = "PROG_ID_SEQ")
    @Column(name = "SRP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "VALUED", length = 127)
    private String value;
    @Column(name = "ODO_PK_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "KRA_FK_ID", length = 11)
    private Integer kpiId;
    @Column(name = "EMG_PK_ID", length = 11)
    private Integer employeeGradeId;
    @Column(name = "OMP_PK_ID", length = 11)
    private Integer orgPositionId;
    @Column(name = "START_DATE", length = 11)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", length = 11)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "EDE_PK_ID", length = 11)
    private Integer employeeId;
    @Column(name = "CMM_FK_ID", length = 11)
    private Integer systemBaseId;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "SDP_FK_ID", length = 11)
    private Integer programId;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SRP_FK_ID", referencedColumnName = "SRP_PK_ID")
    @NotAudited
    private List<KpiRuleConfigMetaData> kpiRuleConfigMetaData;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
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
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public Integer getSystemBaseId() {
        return this.systemBaseId;
    }
    
    public void setSystemBaseId(final Integer systemBaseId) {
        this.systemBaseId = systemBaseId;
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
    
    public List<KpiRuleConfigMetaData> getKpiRuleConfigMetaData() {
        return this.kpiRuleConfigMetaData;
    }
    
    public void setKpiRuleConfigMetaData(final List<KpiRuleConfigMetaData> kpiRuleConfigMetaData) {
        this.kpiRuleConfigMetaData = kpiRuleConfigMetaData;
    }
}

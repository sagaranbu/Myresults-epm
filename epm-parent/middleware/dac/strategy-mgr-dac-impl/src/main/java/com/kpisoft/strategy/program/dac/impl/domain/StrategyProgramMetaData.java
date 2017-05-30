package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import org.hibernate.envers.*;
import com.kpisoft.strategy.dac.impl.domain.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "STR_DET_PROGRAM", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@SQLDelete(sql = "UPDATE STR_DET_PROGRAM SET IS_DELETED = 1 WHERE SDP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyProgramMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = -5409698172122054482L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_PRG_SEQ")
    @SequenceGenerator(name = "STR_PRG_SEQ", sequenceName = "STR_PRG_SEQ")
    @Column(name = "SDP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "START_DATE", length = 127)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", length = 127)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "SDR_PK_ID", length = 127)
    private Integer systemActorId;
    @Column(name = "KMS_PK_ID", length = 127)
    private Integer kpiScaleId;
    @Column(name = "IS_TEMPLATED", length = 127)
    private Boolean isTemplate;
    @Column(name = "STATUS", length = 127)
    private String status;
    @Column(name = "HR_INCHARGE", length = 11)
    private Integer hrIncharge;
    @Column(name = "TARGET_UPDATE_FREQUENCY", length = 11)
    private Integer targetUpdateFrequency;
    @Column(name = "GOAL_SETTING_PERIOD", length = 11)
    private Integer goalSettingPeriod;
    @Column(name = "GOAL_SETTING_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date goalSettingDate;
    @Column(name = "IS_RECURSIVE")
    private Boolean recursive;
    @Column(name = "IS_AUTO_CLOSURE")
    private Boolean autoClosure;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "PERIOD_TYPE_ID")
    private Integer periodTypeId;
    @Column(name = "CASCADE_TYPE")
    private Integer cascadeType;
    @Column(name = "MODE_TYPE")
    private Integer mode;
    @Column(name = "DISPLAY_ORDER_ID")
    private Integer displayOrderId;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SDP_FK_ID", referencedColumnName = "SDP_PK_ID")
    @AuditMappedBy(mappedBy = "programId")
    private List<StrategyProgramPolicyRuleMetaData> strategyProgramPolicyRuleMetaData;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SDP_FK_ID", referencedColumnName = "SDP_PK_ID")
    @AuditMappedBy(mappedBy = "programId")
    private List<ProgramZoneMetaData> programZoneMetaData;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SDP_FK_ID", referencedColumnName = "SDP_PK_ID")
    @AuditMappedBy(mappedBy = "programId")
    private List<ExclusionsMetaData> exclusionsMetaData;
    @ManyToOne
    @JoinColumn(name = "STR_FK_ID", referencedColumnName = "STR_PK_ID")
    private Strategy strategy;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
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
    
    public Integer getSystemActorId() {
        return this.systemActorId;
    }
    
    public void setSystemActorId(final Integer systemActorId) {
        this.systemActorId = systemActorId;
    }
    
    public Boolean getIsTemplate() {
        return this.isTemplate;
    }
    
    public void setIsTemplate(final Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }
    
    public Integer getKpiScaleId() {
        return this.kpiScaleId;
    }
    
    public void setKpiScaleId(final Integer kpiScaleId) {
        this.kpiScaleId = kpiScaleId;
    }
    
    public List<StrategyProgramPolicyRuleMetaData> getStrategyProgramPolicyRuleMetaData() {
        return this.strategyProgramPolicyRuleMetaData;
    }
    
    public void setStrategyProgramPolicyRuleMetaData(final List<StrategyProgramPolicyRuleMetaData> strategyProgramPolicyRuleMetaData) {
        this.strategyProgramPolicyRuleMetaData = strategyProgramPolicyRuleMetaData;
    }
    
    public List<ProgramZoneMetaData> getProgramZoneMetaData() {
        return this.programZoneMetaData;
    }
    
    public void setProgramZoneMetaData(final List<ProgramZoneMetaData> programZoneMetaData) {
        this.programZoneMetaData = programZoneMetaData;
    }
    
    public List<ExclusionsMetaData> getExclusionsMetaData() {
        return this.exclusionsMetaData;
    }
    
    public void setExclusionsMetaData(final List<ExclusionsMetaData> exclusionsMetaData) {
        this.exclusionsMetaData = exclusionsMetaData;
    }
    
    public Strategy getStrategy() {
        return this.strategy;
    }
    
    public void setStrategy(final Strategy strategy) {
        this.strategy = strategy;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public Integer getHrIncharge() {
        return this.hrIncharge;
    }
    
    public void setHrIncharge(final Integer hrIncharge) {
        this.hrIncharge = hrIncharge;
    }
    
    public Integer getTargetUpdateFrequency() {
        return this.targetUpdateFrequency;
    }
    
    public void setTargetUpdateFrequency(final Integer targetUpdateFrequency) {
        this.targetUpdateFrequency = targetUpdateFrequency;
    }
    
    public Integer getGoalSettingPeriod() {
        return this.goalSettingPeriod;
    }
    
    public void setGoalSettingPeriod(final Integer goalSettingPeriod) {
        this.goalSettingPeriod = goalSettingPeriod;
    }
    
    public Date getGoalSettingDate() {
        return this.goalSettingDate;
    }
    
    public void setGoalSettingDate(final Date goalSettingDate) {
        this.goalSettingDate = goalSettingDate;
    }
    
    public Boolean getRecursive() {
        return this.recursive;
    }
    
    public void setRecursive(final Boolean recursive) {
        this.recursive = recursive;
    }
    
    public Boolean getAutoClosure() {
        return this.autoClosure;
    }
    
    public void setAutoClosure(final Boolean autoClosure) {
        this.autoClosure = autoClosure;
    }
    
    public boolean isIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getPeriodTypeId() {
        return this.periodTypeId;
    }
    
    public void setPeriodTypeId(final Integer periodTypeId) {
        this.periodTypeId = periodTypeId;
    }
    
    public Integer getCascadeType() {
        return this.cascadeType;
    }
    
    public void setCascadeType(final Integer cascadeType) {
        this.cascadeType = cascadeType;
    }
    
    public Integer getMode() {
        return this.mode;
    }
    
    public void setMode(final Integer mode) {
        this.mode = mode;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

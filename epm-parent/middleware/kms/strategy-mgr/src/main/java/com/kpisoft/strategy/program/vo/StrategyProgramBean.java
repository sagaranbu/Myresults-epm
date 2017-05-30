package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;
import com.kpisoft.strategy.vo.*;
import java.util.*;

public class StrategyProgramBean extends BaseValueObject
{
    private static final long serialVersionUID = -103042570465971431L;
    private Integer id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer systemActorId;
    private StrategyVo strategy;
    private Integer kpiScaleId;
    private Boolean isTemplate;
    private String status;
    private Integer cascadeType;
    private Integer mode;
    private List<StrategyProgramPolicyRuleBean> strategyProgramPolicyRuleMetaData;
    private List<ExclusionsBean> exclusionsMetaData;
    private List<ProgramZoneBean> programZoneMetaData;
    private Integer hrIncharge;
    private Integer targetUpdateFrequency;
    private Integer goalSettingPeriod;
    private Date goalSettingDate;
    private Boolean recursive;
    private Boolean autoClosure;
    private Integer displayOrderId;
    private Integer periodTypeId;
    
    public StrategyProgramBean() {
        this.strategyProgramPolicyRuleMetaData = new ArrayList<StrategyProgramPolicyRuleBean>();
        this.exclusionsMetaData = new ArrayList<ExclusionsBean>();
        this.programZoneMetaData = new ArrayList<ProgramZoneBean>();
    }
    
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
    
    public StrategyVo getStrategy() {
        return this.strategy;
    }
    
    public void setStrategy(final StrategyVo strategy) {
        this.strategy = strategy;
    }
    
    public Integer getKpiScaleId() {
        return this.kpiScaleId;
    }
    
    public void setKpiScaleId(final Integer kpiScaleId) {
        this.kpiScaleId = kpiScaleId;
    }
    
    public Boolean getIsTemplate() {
        return this.isTemplate;
    }
    
    public void setIsTemplate(final Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }
    
    public List<StrategyProgramPolicyRuleBean> getStrategyProgramPolicyRuleMetaData() {
        return this.strategyProgramPolicyRuleMetaData;
    }
    
    public void setStrategyProgramPolicyRuleMetaData(final List<StrategyProgramPolicyRuleBean> strategyProgramPolicyRuleMetaData) {
        this.strategyProgramPolicyRuleMetaData = strategyProgramPolicyRuleMetaData;
    }
    
    public List<ExclusionsBean> getExclusionsMetaData() {
        return this.exclusionsMetaData;
    }
    
    public void setExclusionsMetaData(final List<ExclusionsBean> exclusionsMetaData) {
        this.exclusionsMetaData = exclusionsMetaData;
    }
    
    public List<ProgramZoneBean> getProgramZoneMetaData() {
        return this.programZoneMetaData;
    }
    
    public void setProgramZoneMetaData(final List<ProgramZoneBean> programZoneMetaData) {
        this.programZoneMetaData = programZoneMetaData;
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
    
    public Integer getPeriodTypeId() {
        return this.periodTypeId;
    }
    
    public void setPeriodTypeId(final Integer periodTypeId) {
        this.periodTypeId = periodTypeId;
    }
}

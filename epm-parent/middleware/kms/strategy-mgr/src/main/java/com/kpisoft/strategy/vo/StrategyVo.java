package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;
import java.util.*;

public class StrategyVo extends BaseValueObject
{
    private static final long serialVersionUID = 7054213314402253579L;
    private Integer id;
    private SystemMasterBaseBean systemMasterBaseData;
    private Integer type;
    private String description;
    private String goal;
    private String objective;
    private String mission;
    private Integer isTemplate;
    private List<StrategyLevelVo> strategyLevel;
    private Integer displayOrderId;
    
    public StrategyVo() {
        this.strategyLevel = new ArrayList<StrategyLevelVo>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public SystemMasterBaseBean getSystemMasterBaseData() {
        return this.systemMasterBaseData;
    }
    
    public void setSystemMasterBaseData(final SystemMasterBaseBean systemMasterBaseData) {
        this.systemMasterBaseData = systemMasterBaseData;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getGoal() {
        return this.goal;
    }
    
    public void setGoal(final String goal) {
        this.goal = goal;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    public void setObjective(final String objective) {
        this.objective = objective;
    }
    
    public String getMission() {
        return this.mission;
    }
    
    public void setMission(final String mission) {
        this.mission = mission;
    }
    
    public Integer getIsTemplate() {
        return this.isTemplate;
    }
    
    public void setIsTemplate(final Integer isTemplate) {
        this.isTemplate = isTemplate;
    }
    
    public List<StrategyLevelVo> getStrategyLevel() {
        return this.strategyLevel;
    }
    
    public void setStrategyLevel(final List<StrategyLevelVo> strategyLevel) {
        this.strategyLevel = strategyLevel;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

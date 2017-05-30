package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;
import java.util.*;

public class StrategyTemplateVo extends BaseValueObject
{
    private static final long serialVersionUID = -3311529066093100528L;
    private Integer id;
    private Integer displayOrderId;
    private SystemMasterBaseBean systemMasterBaseData;
    private Integer type;
    private String description;
    private Integer version;
    private String goal;
    private String vision;
    private String mission;
    private List<StrategyLevelTemplateVo> strategyLevelTemplate;
    
    public StrategyTemplateVo() {
        this.strategyLevelTemplate = new ArrayList<StrategyLevelTemplateVo>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
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
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public String getGoal() {
        return this.goal;
    }
    
    public void setGoal(final String goal) {
        this.goal = goal;
    }
    
    public String getVision() {
        return this.vision;
    }
    
    public void setVision(final String vision) {
        this.vision = vision;
    }
    
    public String getMission() {
        return this.mission;
    }
    
    public void setMission(final String mission) {
        this.mission = mission;
    }
    
    public List<StrategyLevelTemplateVo> getStrategyLevelTemplate() {
        return this.strategyLevelTemplate;
    }
    
    public void setStrategyLevelTemplate(final List<StrategyLevelTemplateVo> strategyLevelTemplate) {
        this.strategyLevelTemplate = strategyLevelTemplate;
    }
}

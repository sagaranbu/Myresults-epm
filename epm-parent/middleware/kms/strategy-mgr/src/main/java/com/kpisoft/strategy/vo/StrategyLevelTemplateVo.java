package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class StrategyLevelTemplateVo extends BaseValueObject
{
    private static final long serialVersionUID = -4571399096640171092L;
    private Integer id;
    private Integer level;
    private Integer minCount;
    private Integer maxCount;
    private String name;
    private String description;
    private Integer version;
    private Integer strTempId;
    private List<StrategyNodeTemplateVo> strategyNodeTemplate;
    
    public StrategyLevelTemplateVo() {
        this.strategyNodeTemplate = new ArrayList<StrategyNodeTemplateVo>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getMinCount() {
        return this.minCount;
    }
    
    public void setMinCount(final Integer minCount) {
        this.minCount = minCount;
    }
    
    public Integer getMaxCount() {
        return this.maxCount;
    }
    
    public void setMaxCount(final Integer maxCount) {
        this.maxCount = maxCount;
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
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getStrTempId() {
        return this.strTempId;
    }
    
    public void setStrTempId(final Integer strTempId) {
        this.strTempId = strTempId;
    }
    
    public List<StrategyNodeTemplateVo> getStrategyNodeTemplate() {
        return this.strategyNodeTemplate;
    }
    
    public void setStrategyNodeTemplate(final List<StrategyNodeTemplateVo> strategyNodeTemplate) {
        this.strategyNodeTemplate = strategyNodeTemplate;
    }
}

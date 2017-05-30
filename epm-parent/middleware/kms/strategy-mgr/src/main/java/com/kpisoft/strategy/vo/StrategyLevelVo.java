package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class StrategyLevelVo extends BaseValueObject
{
    private static final long serialVersionUID = -1787561094124589720L;
    private Integer id;
    private Integer level;
    private Integer minCount;
    private Integer maxCount;
    private String name;
    private String description;
    private Integer strId;
    private List<StrategyNodeVo> strategyNode;
    
    public StrategyLevelVo() {
        this.strategyNode = new ArrayList<StrategyNodeVo>();
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
    
    public Integer getStrId() {
        return this.strId;
    }
    
    public void setStrId(final Integer strId) {
        this.strId = strId;
    }
    
    public List<StrategyNodeVo> getStrategyNode() {
        return this.strategyNode;
    }
    
    public void setStrategyNode(final List<StrategyNodeVo> strategyNode) {
        this.strategyNode = strategyNode;
    }
}

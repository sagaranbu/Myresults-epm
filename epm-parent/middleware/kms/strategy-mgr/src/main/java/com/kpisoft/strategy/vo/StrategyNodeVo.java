package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class StrategyNodeVo extends BaseValueObject
{
    private static final long serialVersionUID = -5737097420983594061L;
    private Integer id;
    private Integer displayOrder;
    private String name;
    private Integer parentId;
    private byte[] image_icon;
    private String code;
    private Integer strLvlId;
    private List<StrategyLevelVo> strategyLevel;
    
    public StrategyNodeVo() {
        this.strategyLevel = new ArrayList<StrategyLevelVo>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }
    
    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public byte[] getImage_icon() {
        return this.image_icon;
    }
    
    public void setImage_icon(final byte[] image_icon) {
        this.image_icon = image_icon;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public Integer getStrLvlId() {
        return this.strLvlId;
    }
    
    public void setStrLvlId(final Integer strLvlId) {
        this.strLvlId = strLvlId;
    }
    
    public List<StrategyLevelVo> getStrategyLevel() {
        return this.strategyLevel;
    }
    
    public void setStrategyLevel(final List<StrategyLevelVo> strategyLevel) {
        this.strategyLevel = strategyLevel;
    }
}

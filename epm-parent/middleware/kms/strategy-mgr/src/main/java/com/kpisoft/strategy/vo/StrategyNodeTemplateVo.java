package com.kpisoft.strategy.vo;

import com.canopus.mw.dto.*;

public class StrategyNodeTemplateVo extends BaseValueObject
{
    private static final long serialVersionUID = -2942544126852289403L;
    private Integer id;
    private Integer displayOrder;
    private Integer parentId;
    private String name;
    private Integer strTmpLvlId;
    
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
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Integer getStrTmpLvlId() {
        return this.strTmpLvlId;
    }
    
    public void setStrTmpLvlId(final Integer strTmpLvlId) {
        this.strTmpLvlId = strTmpLvlId;
    }
}

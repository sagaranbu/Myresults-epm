package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class FieldValidationRuleData extends BaseValueObject
{
    private static final long serialVersionUID = 9094926537607026410L;
    private Integer id;
    private Integer status;
    private String rule;
    private EntityFieldData entityField;
    private boolean isDeleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public String getRule() {
        return this.rule;
    }
    
    public void setRule(final String rule) {
        this.rule = rule;
    }
    
    public EntityFieldData getEntityField() {
        return this.entityField;
    }
    
    public void setEntityField(final EntityFieldData entityField) {
        this.entityField = entityField;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

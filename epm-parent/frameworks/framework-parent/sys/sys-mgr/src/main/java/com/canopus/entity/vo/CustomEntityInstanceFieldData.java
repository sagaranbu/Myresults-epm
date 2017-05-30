package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class CustomEntityInstanceFieldData extends BaseValueObject
{
    private static final long serialVersionUID = 8835718192859094139L;
    private Integer id;
    private Integer entityFieldId;
    private String value;
    private CustomEntityInstanceData entityInstance;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEntityFieldId() {
        return this.entityFieldId;
    }
    
    public void setEntityFieldId(final Integer entityFieldId) {
        this.entityFieldId = entityFieldId;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public CustomEntityInstanceData getEntityInstance() {
        return this.entityInstance;
    }
    
    public void setEntityInstance(final CustomEntityInstanceData entityInstance) {
        this.entityInstance = entityInstance;
    }
}

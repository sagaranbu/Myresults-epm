package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class EntityFieldLangData extends BaseValueObject
{
    private static final long serialVersionUID = 5379907278354502126L;
    private Integer id;
    private String displayName;
    private String description;
    private String helpText;
    private String errorText;
    private String locale;
    private EntityFieldData entityField;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getHelpText() {
        return this.helpText;
    }
    
    public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public EntityFieldData getEntityField() {
        return this.entityField;
    }
    
    public void setEntityField(final EntityFieldData entityField) {
        this.entityField = entityField;
    }
}

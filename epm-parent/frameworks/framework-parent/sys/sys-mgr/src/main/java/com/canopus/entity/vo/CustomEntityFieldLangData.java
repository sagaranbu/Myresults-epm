package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class CustomEntityFieldLangData extends BaseValueObject
{
    private static final long serialVersionUID = -8874809002930160895L;
    private Integer id;
    private String locale;
    private String displayName;
    private String helpText;
    private boolean isDeleted;
    private String errorText;
    private String description;
    private Integer entityFieldId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getHelpText() {
        return this.helpText;
    }
    
    public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Integer getEntityFieldId() {
        return this.entityFieldId;
    }
    
    public void setEntityFieldId(final Integer entityFieldId) {
        this.entityFieldId = entityFieldId;
    }
}

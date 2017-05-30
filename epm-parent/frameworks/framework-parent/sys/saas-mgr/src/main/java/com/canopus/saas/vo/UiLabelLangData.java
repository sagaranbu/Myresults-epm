package com.canopus.saas.vo;

import com.canopus.mw.dto.*;

public class UiLabelLangData extends BaseValueObject
{
    private static final long serialVersionUID = 8437129496804981325L;
    private Integer id;
    private String locale;
    private String displayName;
    private String helpText;
    private String errorText;
    private String description;
    private Integer labelBaseId;
    
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
    
    public Integer getLabelBaseId() {
        return this.labelBaseId;
    }
    
    public void setLabelBaseId(final Integer labelBaseId) {
        this.labelBaseId = labelBaseId;
    }
}

package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class SystemMasterBaseLangData extends BaseValueObject
{
    private static final long serialVersionUID = 1568154279070128350L;
    private Integer id;
    private String errorText;
    private String description;
    private String displayName;
    private String locale;
    private String helpText;
    private Integer sysMasId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
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
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public String getHelpText() {
        return this.helpText;
    }
    
    public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    public Integer getSysMasId() {
        return this.sysMasId;
    }
    
    public void setSysMasId(final Integer sysMasId) {
        this.sysMasId = sysMasId;
    }
}

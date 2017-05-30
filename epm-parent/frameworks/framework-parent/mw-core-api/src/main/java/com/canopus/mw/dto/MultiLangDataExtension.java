package com.canopus.mw.dto;

public class MultiLangDataExtension
{
    private Integer id;
    private Integer mainId;
    private String locale;
    private String name;
    private String description;
    
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
    
    public Integer getMainId() {
        return this.mainId;
    }
    
    public void setMainId(final Integer mainId) {
        this.mainId = mainId;
    }
}

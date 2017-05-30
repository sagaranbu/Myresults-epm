package com.canopus.dac;

import javax.persistence.*;

@MappedSuperclass
public abstract class MultiLangExtension extends BaseDataAccessEntity
{
    @Column(name = "MAIN_ID")
    private Integer mainId;
    @Column(name = "LOCALE", length = 45)
    private String locale;
    @Column(name = "NAME", length = 249)
    private String name;
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    
    public Integer getMainId() {
        return this.mainId;
    }
    
    public void setMainId(final Integer mainId) {
        this.mainId = mainId;
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
}

package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;

@Deprecated
public class OrganizationStructureLanguageData extends BaseValueObject
{
    private static final long serialVersionUID = -3755099425893030764L;
    private Integer id;
    private String name;
    private String description;
    private String localeName;
    private Integer orgStrId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
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
    
    public String getLocaleName() {
        return this.localeName;
    }
    
    public void setLocaleName(final String localeName) {
        this.localeName = localeName;
    }
    
    public Integer getOrgStrId() {
        return this.orgStrId;
    }
    
    public void setOrgStrId(final Integer orgStrId) {
        this.orgStrId = orgStrId;
    }
}

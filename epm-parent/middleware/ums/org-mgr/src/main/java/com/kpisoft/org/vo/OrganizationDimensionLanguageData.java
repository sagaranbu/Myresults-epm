package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;

public class OrganizationDimensionLanguageData extends BaseValueObject
{
    private static final long serialVersionUID = 1818939202117271280L;
    private Integer id;
    private String name;
    private String description;
    private String localeName;
    private boolean isDeleted;
    private OrganizationDimensionData objOrganizationDimension;
    
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
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public OrganizationDimensionData getObjOrganizationDimension() {
        return this.objOrganizationDimension;
    }
    
    public void setObjOrganizationDimension(final OrganizationDimensionData objOrganizationDimension) {
        this.objOrganizationDimension = objOrganizationDimension;
    }
}

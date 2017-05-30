package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import java.io.*;
import javax.validation.constraints.*;
import java.util.*;

public class OrganizationDimensionData extends BaseValueObject implements Serializable
{
    private static final long serialVersionUID = -4046005545842996672L;
    private Integer id;
    @NotNull
    private Boolean defaultDimension;
    private List<OrganizationDimensionStructureData> organizationStructure;
    private List<OrganizationDimensionLanguageData> organizationDimensionLanguage;
    
    public OrganizationDimensionData() {
        this.defaultDimension = false;
        this.organizationStructure = new ArrayList<OrganizationDimensionStructureData>();
        this.organizationDimensionLanguage = new ArrayList<OrganizationDimensionLanguageData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Boolean getDefaultDimension() {
        return this.defaultDimension;
    }
    
    public void setDefaultDimension(final Boolean defaultDimension) {
        this.defaultDimension = defaultDimension;
    }
    
    public List<OrganizationDimensionStructureData> getOrganizationStructure() {
        Collections.sort(this.organizationStructure);
        return this.organizationStructure;
    }
    
    public void setOrganizationStructure(final List<OrganizationDimensionStructureData> organizationStructure) {
        this.organizationStructure = organizationStructure;
    }
    
    public List<OrganizationDimensionLanguageData> getOrganizationDimensionLanguage() {
        return this.organizationDimensionLanguage;
    }
    
    public void setOrganizationDimensionLanguage(final List<OrganizationDimensionLanguageData> organizationDimensionLanguage) {
        this.organizationDimensionLanguage = organizationDimensionLanguage;
    }
}

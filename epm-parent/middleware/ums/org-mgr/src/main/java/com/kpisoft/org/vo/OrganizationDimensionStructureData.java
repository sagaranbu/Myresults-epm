package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;

public class OrganizationDimensionStructureData extends BaseValueObject implements Comparable<OrganizationDimensionStructureData>
{
    private static final long serialVersionUID = 8192330142228285384L;
    private Integer id;
    private Integer level;
    private Integer orgDimId;
    private String baseUrl;
    private String name;
    private String description;
    private String localeName;
    private byte[] image;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getOrgDimId() {
        return this.orgDimId;
    }
    
    public void setOrgDimId(final Integer orgDimId) {
        this.orgDimId = orgDimId;
    }
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
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
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
    
    public int compareTo(final OrganizationDimensionStructureData data) {
        return this.level - data.getLevel();
    }
}

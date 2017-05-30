package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class CustomEntityData extends BaseValueObject
{
    private static final long serialVersionUID = -6872499858703125204L;
    private Integer id;
    private String name;
    private String baseUrl;
    private byte[] image;
    
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
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
}

package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class OperationData extends BaseValueObject
{
    private static final long serialVersionUID = 736083614090472361L;
    private Integer id;
    private Integer status;
    private Integer displayOrderId;
    private Integer version;
    @NotNull
    private String name;
    @NotNull
    private String code;
    private String description;
    private String operationgroup;
    private String baseUrl;
    private String imageURL;
    private byte[] image;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getOperationgroup() {
        return this.operationgroup;
    }
    
    public void setOperationgroup(final String operationgroup) {
        this.operationgroup = operationgroup;
    }
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getImageURL() {
        return this.imageURL;
    }
    
    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
    
    public boolean equals(final String operCode) {
        return this.getCode().contains(operCode);
    }
}

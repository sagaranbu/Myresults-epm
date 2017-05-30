package com.canopus.saas.vo;

import com.canopus.mw.dto.*;

public class TenantContentData extends BaseValueObject
{
    private static final long serialVersionUID = 294026321638793955L;
    private Integer id;
    private String title;
    private String subTitle;
    private String content;
    private Boolean isVisable;
    private Boolean isDynamic;
    private String sourceUrl;
    private Integer type;
    private Integer mode;
    private Integer tenantId;
    private Integer contentOrder;
    private Integer section;
    private Integer isDeleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getSubTitle() {
        return this.subTitle;
    }
    
    public void setSubTitle(final String subTitle) {
        this.subTitle = subTitle;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public Boolean getIsVisable() {
        return this.isVisable;
    }
    
    public void setIsVisable(final Boolean isVisable) {
        this.isVisable = isVisable;
    }
    
    public Boolean getIsDynamic() {
        return this.isDynamic;
    }
    
    public void setIsDynamic(final Boolean isDynamic) {
        this.isDynamic = isDynamic;
    }
    
    public String getSourceUrl() {
        return this.sourceUrl;
    }
    
    public void setSourceUrl(final String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public Integer getMode() {
        return this.mode;
    }
    
    public void setMode(final Integer mode) {
        this.mode = mode;
    }
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getContentOrder() {
        return this.contentOrder;
    }
    
    public void setContentOrder(final Integer contentOrder) {
        this.contentOrder = contentOrder;
    }
    
    public Integer getSection() {
        return this.section;
    }
    
    public void setSection(final Integer section) {
        this.section = section;
    }
    
    public Integer getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}

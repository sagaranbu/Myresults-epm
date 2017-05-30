package com.canopus.saas.dac.entity;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SAS_DET_TENANT_CONTENT")
@SQLDelete(sql = "UPDATE SAS_DET_TENANT_CONTENT SET IS_DELETED = 1 WHERE SDT_PK_ID= ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class TenantContent extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 7740568074859995014L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TENANT_BASE_CONTENT_ID_SEQ")
    @SequenceGenerator(name = "TENANT_BASE_CONTENT_ID_SEQ", sequenceName = "TENANT_BASE_CONTENT_ID_SEQ")
    @Column(name = "SDT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "TITLE", length = 127)
    private String title;
    @Column(name = "SUB_TITLE", length = 127)
    private String subTitle;
    @Column(name = "CONTENT", length = 4000)
    private String content;
    @Column(name = "IS_VISABLE")
    private Boolean isVisable;
    @Column(name = "IS_DYNAMIC")
    private Boolean isDynamic;
    @Column(name = "SOURCE_URL", length = 127)
    private String sourceUrl;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "MODE_NO")
    private Integer mode;
    @Column(name = "SECTION")
    private Integer section;
    @Column(name = "CONTENT_ORDER")
    private Integer contentOrder;
    @Column(name = "IS_DELETED")
    private Integer isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SMT_PK_ID")
    private TenantBase tenantId;
    
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
    
    public TenantBase getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final TenantBase tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getSection() {
        return this.section;
    }
    
    public void setSection(final Integer section) {
        this.section = section;
    }
    
    public Integer getContentOrder() {
        return this.contentOrder;
    }
    
    public void setContentOrder(final Integer contentOrder) {
        this.contentOrder = contentOrder;
    }
    
    public Integer getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}

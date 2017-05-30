package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_MET_DIM_STRUCTURE")
@SQLDelete(sql = "UPDATE ORG_MET_DIM_STRUCTURE SET IS_DELETED = 1 WHERE OMS_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrganizationUnitType extends BaseTenantEntity
{
    private static final long serialVersionUID = -6425292623941693485L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OMS_ID_SEQ")
    @SequenceGenerator(name = "OMS_ID_SEQ", sequenceName = "OMS_ID_SEQ")
    @Column(name = "OMS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "LEVEL_NUM", length = 11)
    private Integer level;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "OMD_FK_ID", length = 11)
    private Integer orgDimId;
    @Column(name = "BASE_URL", length = 127)
    private String baseUrl;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "LOCALE_NAME", length = 127)
    private String localeName;
    @Lob
    @Column(name = "IMAGE")
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
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
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
}

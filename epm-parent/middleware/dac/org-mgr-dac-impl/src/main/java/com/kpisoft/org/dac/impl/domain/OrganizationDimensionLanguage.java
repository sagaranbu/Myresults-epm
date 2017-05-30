package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_MET_DIMENSION_LANG")
@SQLDelete(sql = "UPDATE ORG_MET_DIMENSION_LANG SET IS_DELETED = 1 WHERE OML_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrganizationDimensionLanguage extends BaseTenantEntity
{
    private static final long serialVersionUID = -3186108850216021011L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OML_ID_SEQ")
    @SequenceGenerator(name = "OML_ID_SEQ", sequenceName = "OML_ID_SEQ")
    @Column(name = "OML_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "LOCALE_NAME", length = 127)
    private String localeName;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "OMD_FK_ID", length = 11)
    private Integer orgDimId;
    
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
    
    public Integer getOrgDimId() {
        return this.orgDimId;
    }
    
    public void setOrgDimId(final Integer orgDimId) {
        this.orgDimId = orgDimId;
    }
}

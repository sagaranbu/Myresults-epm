package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import org.hibernate.envers.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_MET_DIMENSION")
@SQLDelete(sql = "UPDATE ORG_MET_DIMENSION SET IS_DELETED = 1 WHERE OMD_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrganizationDimension extends BaseTenantEntity
{
    private static final long serialVersionUID = -3277097232149362030L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OMD_ID_SEQ")
    @SequenceGenerator(name = "OMD_ID_SEQ", sequenceName = "OMD_ID_SEQ")
    @Column(name = "OMD_PK_ID", length = 11)
    private Integer id;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "is_default_dimension", nullable = false)
    private Boolean defaultDimension;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @AuditMappedBy(mappedBy = "orgDimId")
    @JoinColumn(name = "OMD_FK_ID", referencedColumnName = "OMD_PK_ID")
    private List<OrganizationDimensionLanguage> organizationDimensionLanguage;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @AuditMappedBy(mappedBy = "orgDimId")
    @JoinColumn(name = "OMD_FK_ID", referencedColumnName = "OMD_PK_ID")
    @OrderBy("level")
    private List<OrganizationUnitType> organizationStructure;
    
    public OrganizationDimension() {
        this.defaultDimension = false;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Boolean getDefaultDimension() {
        return this.defaultDimension;
    }
    
    public void setDefaultDimension(final Boolean defaultDimension) {
        this.defaultDimension = defaultDimension;
    }
    
    public List<OrganizationDimensionLanguage> getOrganizationDimensionLanguage() {
        return this.organizationDimensionLanguage;
    }
    
    public void setOrganizationDimensionLanguage(final List<OrganizationDimensionLanguage> organizationDimensionLanguage) {
        this.organizationDimensionLanguage = organizationDimensionLanguage;
    }
    
    public List<OrganizationUnitType> getOrganizationStructure() {
        return this.organizationStructure;
    }
    
    public void setOrganizationStructure(final List<OrganizationUnitType> organizationStructure) {
        this.organizationStructure = organizationStructure;
    }
}

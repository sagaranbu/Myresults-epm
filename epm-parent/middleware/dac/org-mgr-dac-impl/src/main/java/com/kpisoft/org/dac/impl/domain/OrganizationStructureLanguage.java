package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_MET_DIM_STRUCTURE_LANG")
@SQLDelete(sql = "UPDATE ORG_MET_DIM_STRUCTURE_LANG SET IS_DELETED = 1 WHERE OMSL_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
@Deprecated
public class OrganizationStructureLanguage extends BaseTenantEntity
{
    private static final long serialVersionUID = -3783615902654028954L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OMSL_ID_SEQ")
    @SequenceGenerator(name = "OMSL_ID_SEQ", sequenceName = "OMSL_ID_SEQ")
    @Column(name = "OMSL_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "LOCALE_NAME", length = 127)
    private String localeName;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "OMS_PK_ID", length = 11)
    private Integer orgStrId;
    
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
    
    public Integer getOrgStrId() {
        return this.orgStrId;
    }
    
    public void setOrgStrId(final Integer orgStrId) {
        this.orgStrId = orgStrId;
    }
}

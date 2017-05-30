package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MET_CATEGORY")
@SQLDelete(sql = "UPDATE KPI_MET_CATEGORY SET IS_DELETED = 1 WHERE KMC_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiCategory extends BaseTenantEntity
{
    private static final long serialVersionUID = -6336613831745884640L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KMC_ID_SEQ")
    @SequenceGenerator(name = "KMC_ID_SEQ", sequenceName = "KMC_ID_SEQ")
    @Column(name = "KMC_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CATEGORY_GROUP", length = 11)
    private Integer categoryGroup;
    @Column(name = "VERSION_NO", length = 45)
    private Integer version;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getCategoryGroup() {
        return this.categoryGroup;
    }
    
    public void setCategoryGroup(final Integer categoryGroup) {
        this.categoryGroup = categoryGroup;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

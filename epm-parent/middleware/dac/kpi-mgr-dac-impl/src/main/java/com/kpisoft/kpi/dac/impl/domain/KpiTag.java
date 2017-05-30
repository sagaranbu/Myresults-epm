package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_TAG")
@SQLDelete(sql = "UPDATE KPI_TAG SET IS_DELETED = 1 WHERE KT_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiTag extends BaseTenantEntity
{
    private static final long serialVersionUID = 5553649015500217132L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_TAG_SEQ")
    @SequenceGenerator(name = "KPI_TAG_SEQ", sequenceName = "KPI_TAG_SEQ", allocationSize = 100)
    @Column(name = "KT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 45)
    private String name;
    @Column(name = "CATEGORY", length = 45)
    private String category;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "KT_PK_ID", referencedColumnName = "KT_PK_ID")
    private List<KpiTagRelationship> kpiTagRelationship;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "DISPLAY_ORDER_ID")
    private Integer displayOrderId;
    
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
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public List<KpiTagRelationship> getKpiTagRelationship() {
        return this.kpiTagRelationship;
    }
    
    public void setKpiTagRelationship(final List<KpiTagRelationship> kpiTagRelationship) {
        this.kpiTagRelationship = kpiTagRelationship;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

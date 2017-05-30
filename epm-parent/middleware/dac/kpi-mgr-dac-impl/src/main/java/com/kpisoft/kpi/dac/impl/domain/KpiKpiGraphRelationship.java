package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_REL_KPI_GRAPH")
@SQLDelete(sql = "UPDATE KPI_REL_KPI_GRAPH SET IS_DELETED = 1 WHERE KKG_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiKpiGraphRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 612992731070133472L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_KPI_REL_GRP_SEQ")
    @SequenceGenerator(name = "KPI_KPI_REL_GRP_SEQ", sequenceName = "KPI_KPI_REL_GRP_SEQ", allocationSize = 100)
    @Column(name = "KKG_PK_ID", length = 11)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "KDB_PK_ID", nullable = false)
    private KpiIdentity parent;
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "CHILD_ID", referencedColumnName = "KDB_PK_ID")
    private KpiIdentity child;
    @Column(name = "TYPE", length = 30, nullable = false)
    private String type;
    @Column(name = "WEIGHTAGE", length = 11)
    private Float weightage;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public KpiIdentity getParent() {
        return this.parent;
    }
    
    public void setParent(final KpiIdentity parent) {
        this.parent = parent;
    }
    
    public KpiIdentity getChild() {
        return this.child;
    }
    
    public void setChild(final KpiIdentity child) {
        this.child = child;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

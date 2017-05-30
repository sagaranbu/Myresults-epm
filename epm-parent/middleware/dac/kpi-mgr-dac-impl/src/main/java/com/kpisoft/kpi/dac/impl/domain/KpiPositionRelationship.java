package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import javax.persistence.*;

public class KpiPositionRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 1609536790414398148L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_POS_REL_SEQ")
    @SequenceGenerator(name = "KPI_POS_REL_SEQ", sequenceName = "KPI_POS_REL_SEQ", allocationSize = 100)
    @Column(name = "KRP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SMR_RELATIONSHIP_FK_ID", length = 11)
    private Integer relationshipBaseId;
    @Column(name = "OMP_POSITION_FK_ID", length = 11)
    private Integer positionId;
    @Column(name = "KDB_KPI_FK_ID", length = 11, insertable = false, updatable = false)
    private Integer kpiId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KDB_KPI_FK_ID", nullable = false)
    private Kpi kpi;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getRelationshipBaseId() {
        return this.relationshipBaseId;
    }
    
    public void setRelationshipBaseId(final Integer relationshipBaseId) {
        this.relationshipBaseId = relationshipBaseId;
    }
    
    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Kpi getKpi() {
        return this.kpi;
    }
    
    public void setKpi(final Kpi kpi) {
        this.kpi = kpi;
    }
}

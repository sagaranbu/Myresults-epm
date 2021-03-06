package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_REL_KPI_SCORECARD")
@SQLDelete(sql = "UPDATE KPI_REL_KPI_SCORECARD SET IS_DELETED = 1 WHERE KRK_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiScorecardRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 4322601328289727847L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_SC_ID_SEQ")
    @SequenceGenerator(name = "KPI_SC_ID_SEQ", sequenceName = "KPI_SC_ID_SEQ", allocationSize = 100)
    @Column(name = "KRK_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SDE_FK_ID")
    private Integer empScorecardId;
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
    
    public Integer getEmpScorecardId() {
        return this.empScorecardId;
    }
    
    public void setEmpScorecardId(final Integer empScorecardId) {
        this.empScorecardId = empScorecardId;
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

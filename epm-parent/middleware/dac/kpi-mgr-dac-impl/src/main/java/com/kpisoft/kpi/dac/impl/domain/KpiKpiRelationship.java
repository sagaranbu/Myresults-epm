package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "KPI_REL_KPI_KPI_RELATIONSHIP")
public class KpiKpiRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -9195860347857542598L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_KPI_REL_SEQ")
    @SequenceGenerator(name = "KPI_KPI_REL_SEQ", sequenceName = "KPI_KPI_REL_SEQ")
    @Column(name = "SKR_PK_ID", length = 11)
    private Integer id;
    @JoinColumn(name = "SOURCE_ID")
    private Integer kpiSourceId;
    @JoinColumn(name = "SMR_RELATIONSHIP_FK_ID")
    private Integer SysEntityRelation;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getKpiSourceId() {
        return this.kpiSourceId;
    }
    
    public void setKpiSourceId(final Integer kpiSourceId) {
        this.kpiSourceId = kpiSourceId;
    }
    
    public Integer getSysEntityRelation() {
        return this.SysEntityRelation;
    }
    
    public void setSysEntityRelation(final Integer sysEntityRelation) {
        this.SysEntityRelation = sysEntityRelation;
    }
}

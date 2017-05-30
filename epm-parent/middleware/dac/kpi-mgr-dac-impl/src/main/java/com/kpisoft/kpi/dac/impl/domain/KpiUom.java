package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MAS_UOM")
@SQLDelete(sql = "UPDATE KPI_MAS_UOM SET IS_DELETED = 1 WHERE KMM_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiUom extends BaseTenantEntity
{
    private static final long serialVersionUID = 5307740201677870952L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KMM_ID_SEQ")
    @SequenceGenerator(name = "KMM_ID_SEQ", sequenceName = "KMM_ID_SEQ")
    @Column(name = "KMM_PK_ID", length = 11)
    private Integer id;
    @Column(name = "UNIT", length = 45)
    private String unit;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "IS_DELETED", length = 11)
    private boolean deleted;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KMA_FK_ID")
    private KpiAggregationType aggrType;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(final String unit) {
        this.unit = unit;
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
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public KpiAggregationType getAggrType() {
        return this.aggrType;
    }
    
    public void setAggrType(final KpiAggregationType aggrType) {
        this.aggrType = aggrType;
    }
}

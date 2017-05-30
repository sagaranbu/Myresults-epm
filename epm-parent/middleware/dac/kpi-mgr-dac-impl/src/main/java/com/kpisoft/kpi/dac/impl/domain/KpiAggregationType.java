package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MET_AGGREGATION_TYPE")
@SQLDelete(sql = "UPDATE KPI_MET_AGGREGATION_TYPE SET IS_DELETED = 1 WHERE KMA_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiAggregationType extends BaseTenantEntity
{
    private static final long serialVersionUID = 7542393008200569124L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_AGGR_ID_SEQ")
    @SequenceGenerator(name = "KPI_AGGR_ID_SEQ", sequenceName = "KPI_AGGR_ID_SEQ")
    @Column(name = "KMA_PK_ID", length = 11)
    private Integer id;
    @Column(name = "type", length = 45)
    private String type;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
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

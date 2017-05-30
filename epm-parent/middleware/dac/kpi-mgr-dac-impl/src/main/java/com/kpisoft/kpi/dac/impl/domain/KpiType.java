package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MAS_TYPE")
@SQLDelete(sql = "UPDATE KPI_MAS_TYPE SET IS_DELETED = 1 WHERE KPI_TYPE_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiType extends BaseTenantEntity
{
    private static final long serialVersionUID = 5147735932886931754L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_TYPE_ID_SEQ")
    @SequenceGenerator(name = "KPI_TYPE_ID_SEQ", sequenceName = "KPI_TYPE_ID_SEQ")
    @Column(name = "KPI_TYPE_ID", length = 11)
    private Integer id;
    @Column(name = "TYPE", length = 45)
    private String type;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
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
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

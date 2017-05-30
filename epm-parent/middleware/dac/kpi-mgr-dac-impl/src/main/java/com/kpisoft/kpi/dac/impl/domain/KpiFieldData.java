package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MET_FIELD_DATA")
@SQLDelete(sql = "UPDATE KPI_MET_FIELD_DATA SET IS_DELETED = 1 WHERE KMF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiFieldData extends BaseTenantEntity
{
    private static final long serialVersionUID = -5503661778039526194L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STR_ID_SEQ")
    @SequenceGenerator(name = "STR_ID_SEQ", sequenceName = "STR_ID_SEQ", allocationSize = 100)
    @Column(name = "KMF_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CMF_FK_ID", length = 11)
    private Integer sysMetFieldId;
    @Column(name = "DATA", length = 4000)
    private String data;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KDB_FK_ID", nullable = false)
    private Kpi kpi;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getSysMetFieldId() {
        return this.sysMetFieldId;
    }
    
    public void setSysMetFieldId(final Integer sysMetFieldId) {
        this.sysMetFieldId = sysMetFieldId;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
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

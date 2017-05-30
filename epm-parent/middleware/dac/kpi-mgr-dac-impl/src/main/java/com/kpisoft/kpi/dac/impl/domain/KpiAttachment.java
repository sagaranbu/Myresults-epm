package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.canopus.entity.domain.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_ATTACHMENT_REL")
@SQLDelete(sql = "UPDATE KPI_ATTACHMENT_REL SET IS_DELETED = 1 WHERE KAR_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiAttachment extends BaseTenantEntity
{
    private static final long serialVersionUID = -3958358880876620914L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_ATT_REL_ID_SEQ")
    @SequenceGenerator(name = "KPI_ATT_REL_ID_SEQ", sequenceName = "KPI_ATT_REL_ID_SEQ", allocationSize = 100)
    @Column(name = "KAR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "KDB_KPI_FK_ID", length = 11, insertable = false, updatable = false)
    private Integer kpiId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KDB_KPI_FK_ID", nullable = false)
    private Kpi kpi;
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(referencedColumnName = "CMA_PK_ID", name = "CMA_FK_ID")
    private SystemAttachment attachment;
    @Column(name = "CONTEXT1", length = 45)
    private String context1;
    @Column(name = "CONTEXT2", length = 45)
    private String context2;
    @Column(name = "CONTEXT3", length = 45)
    private String context3;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public SystemAttachment getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(final SystemAttachment attachment) {
        this.attachment = attachment;
    }
    
    public String getContext1() {
        return this.context1;
    }
    
    public void setContext1(final String context1) {
        this.context1 = context1;
    }
    
    public String getContext2() {
        return this.context2;
    }
    
    public void setContext2(final String context2) {
        this.context2 = context2;
    }
    
    public String getContext3() {
        return this.context3;
    }
    
    public void setContext3(final String context3) {
        this.context3 = context3;
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

package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_TARGET_DATA")
@SQLDelete(sql = "UPDATE KPI_DET_TARGET_DATA SET IS_DELETED = 1 WHERE KDT_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class TargetData extends BaseTenantEntity
{
    private static final long serialVersionUID = 7703923881830797501L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRG_DATA_SEQ")
    @SequenceGenerator(name = "TRG_DATA_SEQ", sequenceName = "TRG_DATA_SEQ", allocationSize = 100)
    @Column(name = "KDT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "TARGET_DATA_NUM", length = 11)
    private Double targetDataNum;
    @Column(name = "KDPM_FK_ID")
    private Integer periodMasterId;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "KMT_FK_ID", length = 11, insertable = false, updatable = false)
    private Integer kpimetTargetId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KMT_FK_ID", nullable = false)
    private KpiTargetMetaData target;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getTargetDataNum() {
        return this.targetDataNum;
    }
    
    public void setTargetDataNum(final Double targetDataNum) {
        this.targetDataNum = targetDataNum;
    }
    
    public Integer getPeriodMasterId() {
        return this.periodMasterId;
    }
    
    public void setPeriodMasterId(final Integer periodMasterId) {
        this.periodMasterId = periodMasterId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getKpimetTargetId() {
        return this.kpimetTargetId;
    }
    
    public void setKpimetTargetId(final Integer kpimetTargetId) {
        this.kpimetTargetId = kpimetTargetId;
    }
    
    public KpiTargetMetaData getTarget() {
        return this.target;
    }
    
    public void setTarget(final KpiTargetMetaData target) {
        this.target = target;
    }
}

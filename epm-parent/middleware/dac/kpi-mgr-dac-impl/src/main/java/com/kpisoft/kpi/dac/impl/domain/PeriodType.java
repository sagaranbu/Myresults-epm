package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.canopus.entity.domain.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_PRD_TYPE")
@SQLDelete(sql = "UPDATE KPI_DET_PRD_TYPE SET IS_DELETED = 1 WHERE KDP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PeriodType extends BaseTenantEntity
{
    private static final long serialVersionUID = -7916244652211934927L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PRD_TYPE_SEQ")
    @SequenceGenerator(name = "PRD_TYPE_SEQ", sequenceName = "PRD_TYPE_SEQ")
    @Column(name = "KDP_PK_ID", length = 11)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    private SystemMasterBaseMetaData systemMasterBase;
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
    
    public SystemMasterBaseMetaData getSystemMasterBase() {
        return this.systemMasterBase;
    }
    
    public void setSystemMasterBase(final SystemMasterBaseMetaData systemMasterBase) {
        this.systemMasterBase = systemMasterBase;
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

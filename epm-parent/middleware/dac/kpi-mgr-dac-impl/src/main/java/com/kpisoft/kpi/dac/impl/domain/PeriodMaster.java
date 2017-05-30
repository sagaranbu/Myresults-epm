package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import com.canopus.entity.domain.SystemMasterBaseMetaData;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_PRD_MAS_BASE")
@SQLDelete(sql = "UPDATE KPI_DET_PRD_MAS_BASE SET IS_DELETED = 1 WHERE KDPM_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PeriodMaster extends BaseTenantEntity
{
    private static final long serialVersionUID = 7312683096421978605L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PRD_MAS_SEQ")
    @SequenceGenerator(name = "PRD_MAS_SEQ", sequenceName = "PRD_MAS_SEQ")
    @Column(name = "KDPM_PK_ID", length = 11)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "KDP_FK_ID")
    private PeriodType periodtype;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    private SystemMasterBaseMetaData systemMasterBase;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @Column(name = "APPLICABLE_PERIOD_IDS", length = 45)
    private String applicablePeriodIds;
    @Column(name = "END_PERIOD_MASTER_ID", length = 11)
    private Integer endPeriodMasterId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PeriodType getPeriodtype() {
        return this.periodtype;
    }
    
    public void setPeriodtype(final PeriodType periodtype) {
        this.periodtype = periodtype;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
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
    
    public String getApplicablePeriodIds() {
        return this.applicablePeriodIds;
    }
    
    public void setApplicablePeriodIds(final String applicablePeriodIds) {
        this.applicablePeriodIds = applicablePeriodIds;
    }
    
    public Integer getEndPeriodMasterId() {
        return this.endPeriodMasterId;
    }
    
    public void setEndPeriodMasterId(final Integer endPeriodMasterId) {
        this.endPeriodMasterId = endPeriodMasterId;
    }
}

package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_REVIEW_FREQUENCY")
@SQLDelete(sql = "UPDATE KPI_DET_REVIEW_FREQUENCY SET IS_DELETED = 1 WHERE KDF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiReviewFrequency extends BaseTenantEntity
{
    private static final long serialVersionUID = 5789421122396487038L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_REVW_FREQ_SEQ")
    @SequenceGenerator(name = "KPI_REVW_FREQ_SEQ", sequenceName = "KPI_REVW_FREQ_SEQ")
    @Column(name = "KDF_PK_ID", length = 11)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KDP_FK_ID", referencedColumnName = "KDP_PK_ID")
    private PeriodType periodType;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PeriodType getPeriodType() {
        return this.periodType;
    }
    
    public void setPeriodType(final PeriodType periodType) {
        this.periodType = periodType;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

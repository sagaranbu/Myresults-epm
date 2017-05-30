package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import com.canopus.entity.domain.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_SCORE")
@SQLDelete(sql = "UPDATE KPI_DET_SCORE SET IS_DELETED = 1 WHERE KDS_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiScore extends BaseTenantEntity
{
    private static final long serialVersionUID = 771126911436556324L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_SCORE_ID_SEQ")
    @SequenceGenerator(name = "KPI_SCORE_ID_SEQ", sequenceName = "KPI_SCORE_ID_SEQ", allocationSize = 100)
    @Column(name = "KDS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "KDB_KPI_FK_ID", length = 11, updatable = false)
    private Integer kpiId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KDB_KPI_FK_ID", insertable = false, updatable = false)
    private Kpi kpi;
    @Column(name = "RATING_LEVEL", length = 11)
    private Integer ratingLevel;
    @Column(name = "NUM_SCORE_ACTUAL", length = 11)
    private Double numScore;
    @Column(name = "NUM_TARGET", length = 11)
    private Double numTarget;
    @Column(name = "NUM_SCORE_ACTUAL_MTD", length = 11)
    private Double numScore_mtd;
    @Column(name = "NUM_TARGET_MTD", length = 11)
    private Double numTarget_mtd;
    @Column(name = "ACHIEVEMENT", length = 11)
    private Double achievement;
    @Column(name = "COMMENTS", length = 127)
    private String comments;
    @Column(name = "SUPERVISOR_COMMENTS", length = 127)
    private String supComments;
    @Column(name = "DATE_TARGET", length = 45)
    @Temporal(TemporalType.DATE)
    private Date dateTarget;
    @Column(name = "DATE_SCORE_ACTUAL", length = 45)
    @Temporal(TemporalType.DATE)
    private Date dateScore;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CMA_FK_ID", referencedColumnName = "CMA_PK_ID")
    private SystemAttachment attachment;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "KDPM_FK_ID", length = 45)
    private Integer periodMasterId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getRatingLevel() {
        return this.ratingLevel;
    }
    
    public void setRatingLevel(final Integer ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
    
    public Double getNumScore() {
        return this.numScore;
    }
    
    public void setNumScore(final Double numScore) {
        this.numScore = numScore;
    }
    
    public Double getNumTarget() {
        return this.numTarget;
    }
    
    public void setNumTarget(final Double numTarget) {
        this.numTarget = numTarget;
    }
    
    public Double getAchievement() {
        return this.achievement;
    }
    
    public void setAchievement(final Double achievement) {
        this.achievement = achievement;
    }
    
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(final String comments) {
        this.comments = comments;
    }
    
    public String getSupComments() {
        return this.supComments;
    }
    
    public void setSupComments(final String supComments) {
        this.supComments = supComments;
    }
    
    public Date getDateTarget() {
        return this.dateTarget;
    }
    
    public void setDateTarget(final Date dateTarget) {
        this.dateTarget = dateTarget;
    }
    
    public Date getDateScore() {
        return this.dateScore;
    }
    
    public void setDateScore(final Date dateScore) {
        this.dateScore = dateScore;
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
    
    public SystemAttachment getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(final SystemAttachment attachment) {
        this.attachment = attachment;
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
    
    public Integer getPeriodMasterId() {
        return this.periodMasterId;
    }
    
    public void setPeriodMasterId(final Integer periodMasterId) {
        this.periodMasterId = periodMasterId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public Double getNumScore_mtd() {
        return this.numScore_mtd;
    }
    
    public void setNumScore_mtd(final Double numScore_mtd) {
        this.numScore_mtd = numScore_mtd;
    }
    
    public Double getNumTarget_mtd() {
        return this.numTarget_mtd;
    }
    
    public void setNumTarget_mtd(final Double numTarget_mtd) {
        this.numTarget_mtd = numTarget_mtd;
    }
}

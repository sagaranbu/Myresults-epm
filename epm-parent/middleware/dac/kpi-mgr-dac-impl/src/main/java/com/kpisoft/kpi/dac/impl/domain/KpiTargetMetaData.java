package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.kpisoft.kpi.vo.*;
import javax.persistence.*;
import javax.persistence.CascadeType;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MET_TARGET")
@SQLDelete(sql = "UPDATE KPI_MET_TARGET SET IS_DELETED = 1 WHERE KMT_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiTargetMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = 7476837044700600745L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_TARGET_SEQ")
    @SequenceGenerator(name = "KPI_TARGET_SEQ", sequenceName = "KPI_TARGET_SEQ", allocationSize = 100)
    @Column(name = "KMT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "FROM_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Column(name = "TO_DATE", length = 11)
    @Temporal(TemporalType.DATE)
    private Date toDate;
    @Column(name = "DATE_TARGET", length = 11)
    @Temporal(TemporalType.DATE)
    private Date dateTarget;
    @Column(name = "NUM_TARGET", length = 11)
    private Double numTarget;
    @Enumerated(EnumType.STRING)
    @Column(name = "TARGET_TYPE", length = 50)
    private TargetCascadeTypeEnum targetType;
    @Enumerated(EnumType.STRING)
    @Column(name = "ACHIVEMENT_TYPE", length = 50)
    private AchivementTypeEnum achievementType;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "target")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<TargetData> targetData;
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "KMS_FK_ID", referencedColumnName = "KMS_PK_ID")
    private Scale scale;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public KpiTargetMetaData() {
        this.targetData = new ArrayList<TargetData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Date getFromDate() {
        return this.fromDate;
    }
    
    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }
    
    public Date getToDate() {
        return this.toDate;
    }
    
    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }
    
    public Date getDateTarget() {
        return this.dateTarget;
    }
    
    public void setDateTarget(final Date dateTarget) {
        this.dateTarget = dateTarget;
    }
    
    public Double getNumTarget() {
        return this.numTarget;
    }
    
    public void setNumTarget(final Double numTarget) {
        this.numTarget = numTarget;
    }
    
    public TargetCascadeTypeEnum getTargetType() {
        return this.targetType;
    }
    
    public void setTargetType(final TargetCascadeTypeEnum targetType) {
        this.targetType = targetType;
    }
    
    public AchivementTypeEnum getAchievementType() {
        return this.achievementType;
    }
    
    public void setAchievementType(final AchivementTypeEnum achievementType) {
        this.achievementType = achievementType;
    }
    
    public List<TargetData> getTargetData() {
        return this.targetData;
    }
    
    public void setTargetData(final List<TargetData> targetData) {
        this.targetData.clear();
        if (targetData != null && !targetData.isEmpty()) {
            for (final TargetData iterator : targetData) {
                this.targetData.add(iterator);
                iterator.setTarget(this);
            }
        }
    }
    
    public Scale getScale() {
        return this.scale;
    }
    
    public void setScale(final Scale scale) {
        this.scale = scale;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

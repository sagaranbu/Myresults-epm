package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.kpisoft.kpi.vo.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.*;

@Entity
@Table(name = "KPI_DET_BASE", uniqueConstraints = { @UniqueConstraint(columnNames = { "CODE", "TENANT_ID" }) })
@SQLDelete(sql = "UPDATE KPI_DET_BASE SET IS_DELETED = 1 WHERE KDB_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Kpi extends BaseTenantEntity
{
    private static final long serialVersionUID = 7681311104959533303L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_ID_SEQ")
    @SequenceGenerator(name = "KPI_ID_SEQ", sequenceName = "KPI_ID_SEQ", allocationSize = 100)
    @Column(name = "KDB_PK_ID", length = 11)
    private Integer id;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "STATE", length = 11)
    private Integer state;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "KPI_OWNER_ID", length = 11)
    private Integer kpiOwnerId;
    @Column(name = "STRN_FK_ID", length = 11)
    private Integer strategyTree;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "GROUP_ID", length = 11)
    private Integer groupId;
    @Column(name = "PRIORITY", length = 11)
    private Integer priority;
    @Column(name = "NUM_TARGET", length = 11)
    private Double numTarget;
    @Column(name = "DATE_TARGET", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTarget;
    @Column(name = "CURR_RATING_LEVEL", length = 11)
    private Integer currRatingLevel;
    @Column(name = "CURR_ACHIEVEMENT", length = 11)
    private Double currAchievement;
    @Column(name = "CURR_DATE_SCORE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date currDateScore;
    @Column(name = "CURR_SCORE", length = 11)
    private Double currScore;
    @Column(name = "CURR_PERIODID", length = 11)
    private Integer currPeriodId;
    @Column(name = "WEIGHTAGE", length = 11)
    private Float weightage;
    @Column(name = "CODE", length = 45)
    private String code;
    @Column(name = "ROOT_CODE", length = 11)
    private String rootCode;
    @Column(name = "NAME", length = 45)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "IS_GROUP")
    private Boolean groupKpi;
    @Column(name = "IS_CORE")
    private Boolean coreKpi;
    @Column(name = "IS_INDIVIDUAL")
    private Boolean individualKpi;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "DEPTH", length = 50)
    private DepthEnum depth;
    @Enumerated(EnumType.STRING)
    @Column(name = "BREADTH", length = 50)
    private BreadthEnum breadth;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KPI_UOM_FK_ID")
    private KpiUom uom;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KMA_FK_ID")
    private KpiAggregationType aggregationType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KPI_TYPE_FK_ID")
    private KpiType kpiType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KPI_CATEGORY_ID")
    private KpiCategory kpiCategory;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KPI_ORIGINATION_TYPE_ID")
    private KpiOrgType kpiOrgType;
    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinColumn(name = "KMT_FK_ID", nullable = true)
    private KpiTargetMetaData kpiTarget;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KDF_FK_ID", referencedColumnName = "KDF_PK_ID", nullable = true)
    private KpiReviewFrequency reviewFrequency;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "kpi")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiScorecardRelationship> kpiScorecardRelationships;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "kpi")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiTagRelationship> kpiTagRelationship;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "kpi")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiAttachment> attachmentList;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "kpi")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiFieldData> fieldData;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "child")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiKpiGraphRelationship> kpiKpiRelationship;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "kpi")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<KpiScore> kpiScore;
    
    public Kpi() {
        this.kpiScorecardRelationships = new ArrayList<KpiScorecardRelationship>();
        this.kpiTagRelationship = new ArrayList<KpiTagRelationship>();
        this.attachmentList = new ArrayList<KpiAttachment>();
        this.fieldData = new ArrayList<KpiFieldData>();
        this.kpiKpiRelationship = new ArrayList<KpiKpiGraphRelationship>();
        this.kpiScore = new ArrayList<KpiScore>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public Integer getState() {
        return this.state;
    }
    
    public void setState(final Integer state) {
        this.state = state;
    }
    
    public Integer getKpiOwnerId() {
        return this.kpiOwnerId;
    }
    
    public void setKpiOwnerId(final Integer kpiOwnerId) {
        this.kpiOwnerId = kpiOwnerId;
    }
    
    public Integer getStrategyTree() {
        return this.strategyTree;
    }
    
    public void setStrategyTree(final Integer strategyTree) {
        this.strategyTree = strategyTree;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    
    public void setPriority(final Integer priority) {
        this.priority = priority;
    }
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public String getRootCode() {
        return this.rootCode;
    }
    
    public void setRootCode(final String rootCode) {
        this.rootCode = rootCode;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Boolean getGroupKpi() {
        return this.groupKpi;
    }
    
    public void setGroupKpi(final Boolean groupKpi) {
        this.groupKpi = groupKpi;
    }
    
    public Boolean getCoreKpi() {
        return this.coreKpi;
    }
    
    public void setCoreKpi(final Boolean coreKpi) {
        this.coreKpi = coreKpi;
    }
    
    public Boolean getIndividualKpi() {
        return this.individualKpi;
    }
    
    public void setIndividualKpi(final Boolean individualKpi) {
        this.individualKpi = individualKpi;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
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
    
    public DepthEnum getDepth() {
        return this.depth;
    }
    
    public void setDepth(final DepthEnum depth) {
        this.depth = depth;
    }
    
    public BreadthEnum getBreadth() {
        return this.breadth;
    }
    
    public void setBreadth(final BreadthEnum breadth) {
        this.breadth = breadth;
    }
    
    public KpiUom getUom() {
        return this.uom;
    }
    
    public void setUom(final KpiUom uom) {
        this.uom = uom;
    }
    
    public KpiAggregationType getAggregationType() {
        return this.aggregationType;
    }
    
    public void setAggregationType(final KpiAggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }
    
    public KpiType getKpiType() {
        return this.kpiType;
    }
    
    public void setKpiType(final KpiType kpiType) {
        this.kpiType = kpiType;
    }
    
    public KpiCategory getKpiCategory() {
        return this.kpiCategory;
    }
    
    public void setKpiCategory(final KpiCategory kpiCategory) {
        this.kpiCategory = kpiCategory;
    }
    
    public KpiOrgType getKpiOrgType() {
        return this.kpiOrgType;
    }
    
    public void setKpiOrgType(final KpiOrgType kpiOrgType) {
        this.kpiOrgType = kpiOrgType;
    }
    
    public KpiTargetMetaData getKpiTarget() {
        return this.kpiTarget;
    }
    
    public void setKpiTarget(final KpiTargetMetaData kpiTarget) {
        this.kpiTarget = kpiTarget;
    }
    
    public KpiReviewFrequency getReviewFrequency() {
        return this.reviewFrequency;
    }
    
    public void setReviewFrequency(final KpiReviewFrequency reviewFrequency) {
        this.reviewFrequency = reviewFrequency;
    }
    
    public List<KpiScorecardRelationship> getKpiScorecardRelationships() {
        return Collections.unmodifiableList((List<? extends KpiScorecardRelationship>)this.kpiScorecardRelationships);
    }
    
    public void setKpiScorecardRelationships(final List<KpiScorecardRelationship> kpiScorecardRelationships) {
        this.kpiScorecardRelationships.clear();
        if (kpiScorecardRelationships != null && !kpiScorecardRelationships.isEmpty()) {
            for (final KpiScorecardRelationship iterator : kpiScorecardRelationships) {
                this.kpiScorecardRelationships.add(iterator);
                iterator.setKpi(this);
            }
        }
    }
    
    public List<KpiTagRelationship> getKpiTagRelationship() {
        return Collections.unmodifiableList((List<? extends KpiTagRelationship>)this.kpiTagRelationship);
    }
    
    public void setKpiTagRelationship(final List<KpiTagRelationship> kpiTagRelationship) {
        this.kpiTagRelationship.clear();
        if (kpiTagRelationship != null && !kpiTagRelationship.isEmpty()) {
            for (final KpiTagRelationship iterator : kpiTagRelationship) {
                this.kpiTagRelationship.add(iterator);
                iterator.setKpi(this);
            }
        }
    }
    
    public List<KpiAttachment> getAttachmentList() {
        return Collections.unmodifiableList((List<? extends KpiAttachment>)this.attachmentList);
    }
    
    public void setAttachmentList(final List<KpiAttachment> attachmentList) {
        this.attachmentList.clear();
        if (attachmentList != null && !attachmentList.isEmpty()) {
            for (final KpiAttachment iterator : attachmentList) {
                this.attachmentList.add(iterator);
                iterator.setKpi(this);
            }
        }
    }
    
    public List<KpiFieldData> getFieldData() {
        return Collections.unmodifiableList((List<? extends KpiFieldData>)this.fieldData);
    }
    
    public void setFieldData(final List<KpiFieldData> fieldData) {
        this.fieldData.clear();
        if (fieldData != null && !fieldData.isEmpty()) {
            for (final KpiFieldData iterator : fieldData) {
                this.fieldData.add(iterator);
                iterator.setKpi(this);
            }
        }
    }
    
    public List<KpiScore> getKpiScore() {
        return Collections.unmodifiableList((List<? extends KpiScore>)this.kpiScore);
    }
    
    public void setKpiScore(final List<KpiScore> kpiScore) {
        this.kpiScore.clear();
        if (kpiScore != null && !kpiScore.isEmpty()) {
            for (final KpiScore iterator : kpiScore) {
                this.kpiScore.add(iterator);
                iterator.setKpi(this);
            }
        }
    }
    
    public List<KpiKpiGraphRelationship> getKpiKpiRelationship() {
        return Collections.unmodifiableList((List<? extends KpiKpiGraphRelationship>)this.kpiKpiRelationship);
    }
    
    public void setKpiKpiRelationship(final List<KpiKpiGraphRelationship> kpiKpiRelationship) {
        this.kpiKpiRelationship.clear();
        if (kpiKpiRelationship != null && !kpiKpiRelationship.isEmpty()) {
            for (final KpiKpiGraphRelationship iterator : kpiKpiRelationship) {
                this.kpiKpiRelationship.add(iterator);
            }
        }
    }
    
    public Double getNumTarget() {
        return this.numTarget;
    }
    
    public void setNumTarget(final Double numTarget) {
        this.numTarget = numTarget;
    }
    
    public Date getDateTarget() {
        return this.dateTarget;
    }
    
    public void setDateTarget(final Date dateTarget) {
        this.dateTarget = dateTarget;
    }
    
    public Integer getCurrRatingLevel() {
        return this.currRatingLevel;
    }
    
    public void setCurrRatingLevel(final Integer currRatingLevel) {
        this.currRatingLevel = currRatingLevel;
    }
    
    public Double getCurrAchievement() {
        return this.currAchievement;
    }
    
    public void setCurrAchievement(final Double currAchievement) {
        this.currAchievement = currAchievement;
    }
    
    public Date getCurrDateScore() {
        return this.currDateScore;
    }
    
    public void setCurrDateScore(final Date currDateScore) {
        this.currDateScore = currDateScore;
    }
    
    public Double getCurrScore() {
        return this.currScore;
    }
    
    public void setCurrScore(final Double currScore) {
        this.currScore = currScore;
    }
    
    public Integer getCurrPeriodId() {
        return this.currPeriodId;
    }
    
    public void setCurrPeriodId(final Integer currPeriodId) {
        this.currPeriodId = currPeriodId;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
}

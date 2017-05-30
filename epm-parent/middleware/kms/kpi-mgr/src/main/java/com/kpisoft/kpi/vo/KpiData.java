package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.text.*;
import java.util.*;

public class KpiData extends BaseValueObject implements Comparable<KpiData>
{
    private static final long serialVersionUID = -7241147701870929677L;
    DecimalFormat df;
    private Integer id;
    private Integer parentId;
    private Integer state;
    private Integer status;
    private Integer strategyTree;
    private Integer version;
    private Integer kpiOwnerId;
    private Integer groupId;
    private Integer priority;
    private Double numTarget;
    private Date dateTarget;
    private Integer currRatingLevel;
    private Double currAchievement;
    private Date currDateScore;
    private Double currScore;
    private Integer currPeriodId;
    private Float weightage;
    private String code;
    private String rootCode;
    private String name;
    private String description;
    private Boolean groupKpi;
    private Boolean coreKpi;
    private Boolean individualKpi;
    private Date startDate;
    private Date endDate;
    private DepthEnum depth;
    private BreadthEnum breadth;
    private KpiUomData uom;
    private KpiAggregationTypeBean aggregationType;
    private KpiTypeData kpiType;
    private KpiCategoryData kpiCategory;
    private KpiOrgTypeData kpiOrgType;
    private KpiTargetBean kpiTarget;
    private KpiReviewFrequencyBean reviewFrequency;
    private ScorecardBean scorecardBean;
    private List<KpiKpiGraphRelationshipBean> kpiKpiRelationship;
    private List<KpiEmployeeRelationshipBean> kpiEmpRelationship;
    private List<KpiPositionRelationshipBean> kpiPositionRelationship;
    private List<KpiScorecardRelationshipBean> kpiScorecardRelationships;
    private List<KpiTagRelationshipBean> kpiTagRelationship;
    private List<KpiScoreBean> kpiScore;
    private List<KpiAttachmentBean> attachmentList;
    private List<KpiFieldDataBean> fieldData;
    
    public KpiData() {
        this.df = new DecimalFormat("#.##");
        this.status = new Integer(1);
        this.kpiKpiRelationship = new ArrayList<KpiKpiGraphRelationshipBean>();
        this.kpiEmpRelationship = new ArrayList<KpiEmployeeRelationshipBean>();
        this.kpiPositionRelationship = new ArrayList<KpiPositionRelationshipBean>();
        this.kpiScorecardRelationships = new ArrayList<KpiScorecardRelationshipBean>();
        this.kpiTagRelationship = new ArrayList<KpiTagRelationshipBean>();
        this.kpiScore = new ArrayList<KpiScoreBean>();
        this.attachmentList = new ArrayList<KpiAttachmentBean>();
        this.fieldData = new ArrayList<KpiFieldDataBean>();
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
    
    public Integer getKpiOwnerId() {
        return this.kpiOwnerId;
    }
    
    public void setKpiOwnerId(final Integer kpiOwnerId) {
        this.kpiOwnerId = kpiOwnerId;
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
    
    public KpiUomData getUom() {
        return this.uom;
    }
    
    public void setUom(final KpiUomData uom) {
        this.uom = uom;
    }
    
    public KpiAggregationTypeBean getAggregationType() {
        return this.aggregationType;
    }
    
    public void setAggregationType(final KpiAggregationTypeBean aggregationType) {
        this.aggregationType = aggregationType;
    }
    
    public KpiTypeData getKpiType() {
        return this.kpiType;
    }
    
    public void setKpiType(final KpiTypeData kpiType) {
        this.kpiType = kpiType;
    }
    
    public KpiCategoryData getKpiCategory() {
        return this.kpiCategory;
    }
    
    public void setKpiCategory(final KpiCategoryData kpiCategory) {
        this.kpiCategory = kpiCategory;
    }
    
    public KpiOrgTypeData getKpiOrgType() {
        return this.kpiOrgType;
    }
    
    public void setKpiOrgType(final KpiOrgTypeData kpiOrgType) {
        this.kpiOrgType = kpiOrgType;
    }
    
    public KpiTargetBean getKpiTarget() {
        return this.kpiTarget;
    }
    
    public void setKpiTarget(final KpiTargetBean kpiTarget) {
        this.kpiTarget = kpiTarget;
    }
    
    public KpiReviewFrequencyBean getReviewFrequency() {
        return this.reviewFrequency;
    }
    
    public void setReviewFrequency(final KpiReviewFrequencyBean reviewFrequency) {
        this.reviewFrequency = reviewFrequency;
    }
    
    public ScorecardBean getScorecardBean() {
        return this.scorecardBean;
    }
    
    public void setScorecardBean(final ScorecardBean scorecardBean) {
        this.scorecardBean = scorecardBean;
    }
    
    public List<KpiKpiGraphRelationshipBean> getKpiKpiRelationship() {
        return this.kpiKpiRelationship;
    }
    
    public void setKpiKpiRelationship(final List<KpiKpiGraphRelationshipBean> kpiKpiRelationship) {
        this.kpiKpiRelationship = kpiKpiRelationship;
    }
    
    public List<KpiEmployeeRelationshipBean> getKpiEmpRelationship() {
        return this.kpiEmpRelationship;
    }
    
    public void setKpiEmpRelationship(final List<KpiEmployeeRelationshipBean> kpiEmpRelationship) {
        this.kpiEmpRelationship = kpiEmpRelationship;
    }
    
    public List<KpiPositionRelationshipBean> getKpiPositionRelationship() {
        return this.kpiPositionRelationship;
    }
    
    public void setKpiPositionRelationship(final List<KpiPositionRelationshipBean> kpiPositionRelationship) {
        this.kpiPositionRelationship = kpiPositionRelationship;
    }
    
    public List<KpiScorecardRelationshipBean> getKpiScorecardRelationships() {
        return this.kpiScorecardRelationships;
    }
    
    public void setKpiScorecardRelationships(final List<KpiScorecardRelationshipBean> kpiScorecardRelationships) {
        this.kpiScorecardRelationships = kpiScorecardRelationships;
    }
    
    public List<KpiTagRelationshipBean> getKpiTagRelationship() {
        return this.kpiTagRelationship;
    }
    
    public void setKpiTagRelationship(final List<KpiTagRelationshipBean> kpiTagRelationship) {
        this.kpiTagRelationship = kpiTagRelationship;
    }
    
    public List<KpiScoreBean> getKpiScore() {
        Collections.sort(this.kpiScore);
        return this.kpiScore;
    }
    
    public void setKpiScore(final List<KpiScoreBean> kpiScore) {
        this.kpiScore = kpiScore;
    }
    
    public List<KpiAttachmentBean> getAttachmentList() {
        return this.attachmentList;
    }
    
    public void setAttachmentList(final List<KpiAttachmentBean> attachmentList) {
        this.attachmentList = attachmentList;
    }
    
    public List<KpiFieldDataBean> getFieldData() {
        return this.fieldData;
    }
    
    public void setFieldData(final List<KpiFieldDataBean> fieldData) {
        this.fieldData = fieldData;
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
        if (this.currAchievement != null) {
            this.currAchievement = Double.valueOf(this.df.format(this.currAchievement));
        }
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
    
    public String toString() {
        return "{" + this.id + ": " + this.name + '}';
    }
    
    public int compareTo(final KpiData t) {
        if (t == null) {
            return 1;
        }
        if (t.getId() == null) {
            return 1;
        }
        if (this.getId() == null) {
            return -1;
        }
        final int compareId = t.getId();
        return this.getId() - compareId;
    }
}

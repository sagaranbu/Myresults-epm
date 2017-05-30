package com.kpisoft.kpi.utility;

import com.canopus.mw.dto.*;

public class ScorecardCalculatorUtility extends BaseValueObject
{
    private static final long serialVersionUID = 5605154505151362339L;
    private Float weightage;
    private Integer ratingLevel;
    private Integer kpiId;
    private Boolean isGroupKpi;
    private Integer groupId;
    private Integer scoreId;
    private Double achievement;
    private Double avgRatingLevel;
    private Integer scorecardId;
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
    
    public Integer getRatingLevel() {
        return this.ratingLevel;
    }
    
    public void setRatingLevel(final Integer ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public Boolean getIsGroupKpi() {
        return this.isGroupKpi;
    }
    
    public void setIsGroupKpi(final Boolean isGroupKpi) {
        this.isGroupKpi = isGroupKpi;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
    
    public Integer getScoreId() {
        return this.scoreId;
    }
    
    public void setScoreId(final Integer scoreId) {
        this.scoreId = scoreId;
    }
    
    public Double getAchievement() {
        return this.achievement;
    }
    
    public void setAchievement(final Double achievement) {
        this.achievement = achievement;
    }
    
    public Double getAvgRatingLevel() {
        return this.avgRatingLevel;
    }
    
    public void setAvgRatingLevel(final Double avgRatingLevel) {
        this.avgRatingLevel = avgRatingLevel;
    }
    
    public Integer getScorecardId() {
        return this.scorecardId;
    }
    
    public void setScorecardId(final Integer scorecardId) {
        this.scorecardId = scorecardId;
    }
}

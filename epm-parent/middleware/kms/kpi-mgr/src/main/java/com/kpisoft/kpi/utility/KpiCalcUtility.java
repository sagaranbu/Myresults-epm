package com.kpisoft.kpi.utility;

import com.canopus.mw.dto.*;

public class KpiCalcUtility extends BaseValueObject
{
    private static final long serialVersionUID = -5210840334697105551L;
    private Double achievement;
    private Integer ratingLevel;
    
    public Double getAchievement() {
        return this.achievement;
    }
    
    public void setAchievement(final Double achievement) {
        this.achievement = achievement;
    }
    
    public Integer getRatingLevel() {
        return this.ratingLevel;
    }
    
    public void setRatingLevel(final Integer ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}

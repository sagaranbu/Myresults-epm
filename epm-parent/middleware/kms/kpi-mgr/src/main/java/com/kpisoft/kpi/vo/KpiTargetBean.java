package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class KpiTargetBean extends BaseValueObject
{
    private static final long serialVersionUID = 8172880154169695993L;
    private Integer id;
    private Double numTarget;
    private Date fromDate;
    private Date toDate;
    private Date dateTarget;
    private TargetCascadeTypeEnum targetType;
    private AchivementTypeEnum achievementType;
    private KpiScaleBean scale;
    private List<KpiTargetDataBean> targetData;
    
    public KpiTargetBean() {
        this.targetData = new ArrayList<KpiTargetDataBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getNumTarget() {
        return this.numTarget;
    }
    
    public void setNumTarget(final Double numTarget) {
        this.numTarget = numTarget;
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
    
    public KpiScaleBean getScale() {
        return this.scale;
    }
    
    public void setScale(final KpiScaleBean scale) {
        this.scale = scale;
    }
    
    public List<KpiTargetDataBean> getTargetData() {
        Collections.sort(this.targetData);
        return this.targetData;
    }
    
    public void setTargetData(final List<KpiTargetDataBean> targetData) {
        this.targetData = targetData;
    }
}

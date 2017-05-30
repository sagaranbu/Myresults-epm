package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.text.*;
import java.util.*;
import com.canopus.entity.vo.*;

public class KpiScoreBean extends BaseValueObject implements Comparable<KpiScoreBean>
{
    DecimalFormat df;
    private static final long serialVersionUID = -4334165066042951991L;
    private Integer id;
    private Integer ratingLevel;
    private Double numScore;
    private Double numTarget;
    private Double numScore_mtd;
    private Double numTarget_mtd;
    private Double achievement;
    private String comments;
    private String supComments;
    private Date dateTarget;
    private Date startDate;
    private Date endDate;
    private Date dateScore;
    private SystemAttachmentBean attachment;
    private Integer periodMasterId;
    private Integer kpiId;
    
    public KpiScoreBean() {
        this.df = new DecimalFormat("#.##");
    }
    
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
        if (this.achievement != null) {
            this.achievement = Double.valueOf(this.df.format(this.achievement));
        }
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
    
    public SystemAttachmentBean getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(final SystemAttachmentBean attachment) {
        this.attachment = attachment;
    }
    
    public Date getDateScore() {
        return this.dateScore;
    }
    
    public void setDateScore(final Date dateScore) {
        this.dateScore = dateScore;
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
    
    public int compareTo(final KpiScoreBean t) {
        if (this.getPeriodMasterId() != null && t.getPeriodMasterId() != null) {
            return this.getPeriodMasterId() - t.getPeriodMasterId();
        }
        if (this.getPeriodMasterId() != null) {
            return this.getPeriodMasterId();
        }
        if (t.getPeriodMasterId() != null) {
            return t.getPeriodMasterId();
        }
        return 0;
    }
}

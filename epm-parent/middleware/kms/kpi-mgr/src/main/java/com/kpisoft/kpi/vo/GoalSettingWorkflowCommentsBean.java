package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class GoalSettingWorkflowCommentsBean extends BaseValueObject
{
    private static final long serialVersionUID = -6449998769449344821L;
    private Integer id;
    @NotNull
    private Integer scorecardId;
    @NotNull
    private Integer reviewerId;
    @NotNull
    private Integer workflowId;
    private Integer kpiId;
    private Integer tagId;
    private String comments;
    private String status;
    private Date reviewedDate;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getScorecardId() {
        return this.scorecardId;
    }
    
    public void setScorecardId(final Integer scorecardId) {
        this.scorecardId = scorecardId;
    }
    
    public Integer getReviewerId() {
        return this.reviewerId;
    }
    
    public void setReviewerId(final Integer reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public Integer getWorkflowId() {
        return this.workflowId;
    }
    
    public void setWorkflowId(final Integer workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(final String comments) {
        this.comments = comments;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public Date getReviewedDate() {
        return this.reviewedDate;
    }
    
    public void setReviewedDate(final Date reviewedDate) {
        this.reviewedDate = reviewedDate;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public Integer getTagId() {
        return this.tagId;
    }
    
    public void setTagId(final Integer tagId) {
        this.tagId = tagId;
    }
}

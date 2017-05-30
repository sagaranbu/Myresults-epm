package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class PerformanceReviewCommentsBean extends BaseValueObject
{
    private static final long serialVersionUID = -3836535894340981735L;
    private Integer id;
    @NotNull
    private Integer scoreId;
    @NotNull
    private Integer reviewerId;
    @NotNull
    private Integer workflowId;
    private String comments;
    private String status;
    private Date reviewedDate;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getScoreId() {
        return this.scoreId;
    }
    
    public void setScoreId(final Integer scoreId) {
        this.scoreId = scoreId;
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
}

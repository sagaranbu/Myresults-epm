package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class ScorecardScoreBean extends BaseValueObject
{
    private static final long serialVersionUID = -3640008515255709359L;
    private Integer id;
    private Double score;
    private Date reviewDate;
    private Integer rating;
    private String comment;
    private Date dueDate;
    private Integer verifiedBy;
    private Integer reviewedBy;
    private ScorecardBean scorecard;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getScore() {
        return this.score;
    }
    
    public void setScore(final Double score) {
        this.score = score;
    }
    
    public Date getReviewDate() {
        return this.reviewDate;
    }
    
    public void setReviewDate(final Date reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public Integer getRating() {
        return this.rating;
    }
    
    public void setRating(final Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Integer getVerifiedBy() {
        return this.verifiedBy;
    }
    
    public void setVerifiedBy(final Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    public Integer getReviewedBy() {
        return this.reviewedBy;
    }
    
    public void setReviewedBy(final Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public ScorecardBean getScorecard() {
        return this.scorecard;
    }
    
    public void setScorecard(final ScorecardBean scorecard) {
        this.scorecard = scorecard;
    }
}

package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "STR_DET_SCARD_SCORE")
@SQLDelete(sql = "UPDATE STR_DET_SCARD_SCORE SET IS_DELETED = 1 WHERE SDS_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ScorecardScore extends BaseTenantEntity
{
    private static final long serialVersionUID = -2167638712980985613L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCARD_SCORE_ID_SEQ")
    @SequenceGenerator(name = "SCARD_SCORE_ID_SEQ", sequenceName = "SCARD_SCORE_ID_SEQ", allocationSize = 100)
    @Column(name = "SDS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SCORE", length = 11)
    private Double score;
    @Column(name = "REVIEW_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;
    @Column(name = "RATING", length = 11)
    private Integer rating;
    @Column(name = "COMMENTS", length = 127)
    private String comment;
    @Column(name = "DUE_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "VERIFIED_BY", length = 11)
    private Integer verifiedBy;
    @Column(name = "REVIEWED_BY", length = 11)
    private Integer reviewedBy;
    @ManyToOne
    @JoinColumn(name = "SDE_PK_ID")
    private ScorecardMetaData scorecard;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
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
    
    public ScorecardMetaData getScorecard() {
        return this.scorecard;
    }
    
    public void setScorecard(final ScorecardMetaData scorecard) {
        this.scorecard = scorecard;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

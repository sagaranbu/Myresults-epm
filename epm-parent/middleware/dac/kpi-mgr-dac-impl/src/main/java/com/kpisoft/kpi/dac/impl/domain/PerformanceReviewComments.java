package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PRFM_REVIEW_COMMENTS")
@SQLDelete(sql = "UPDATE PRFM_REVIEW_COMMENTS SET IS_DELETED = 1 WHERE PRC_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PerformanceReviewComments extends BaseTenantEntity
{
    private static final long serialVersionUID = 6868973299690436102L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PRC_ID_SEQ")
    @SequenceGenerator(name = "PRC_ID_SEQ", sequenceName = "PRC_ID_SEQ")
    @Column(name = "PRC_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SCORE_ID", length = 11)
    private Integer scoreId;
    @Column(name = "REVIEWER_ID")
    private Integer reviewerId;
    @Column(name = "WORKFLOW_ID", length = 11)
    private Integer workflowId;
    @Column(name = "COMMENTS", length = 1000)
    private String comments;
    @Column(name = "STATUS", length = 127)
    private String status;
    @Column(name = "REVIEWED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedDate;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

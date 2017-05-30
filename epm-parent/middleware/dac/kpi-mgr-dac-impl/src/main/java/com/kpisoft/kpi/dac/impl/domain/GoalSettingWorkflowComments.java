package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GOAL_SETTING_WORKFLOW_COMMENTS")
@SQLDelete(sql = "UPDATE GOAL_SETTING_WORKFLOW_COMMENTS SET IS_DELETED = 1 WHERE GSWC_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class GoalSettingWorkflowComments extends BaseTenantEntity
{
    private static final long serialVersionUID = -9033435704408765017L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GSWC_ID_SEQ")
    @SequenceGenerator(name = "GSWC_ID_SEQ", sequenceName = "GSWC_ID_SEQ")
    @Column(name = "GSWC_PK_ID", length = 11)
    private Integer id;
    @Index(name = "GSWC_IDX_SCORECARD")
    @Column(name = "SDE_FK_ID", length = 11)
    private Integer scorecardId;
    @Column(name = "REVIEWER_ID")
    private Integer reviewerId;
    @Column(name = "WORKFLOW_ID", length = 11)
    private Integer workflowId;
    @Column(name = "COMMENTS", length = 1000)
    private String comments;
    @Index(name = "GSWC_IDX_KPI")
    @Column(name = "KDB_FK_ID", length = 11)
    private Integer kpiId;
    @Column(name = "TAG_ID", length = 11)
    private Integer tagId;
    @Column(name = "STATUS", length = 11)
    private Integer status;
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
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
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

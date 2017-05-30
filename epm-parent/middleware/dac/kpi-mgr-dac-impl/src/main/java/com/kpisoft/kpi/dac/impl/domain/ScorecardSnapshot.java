package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_DET_SCARD_SNAPSHOT")
@SQLDelete(sql = "UPDATE KPI_DET_SCARD_SNAPSHOT SET IS_DELETED = 1 WHERE SSN_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ScorecardSnapshot extends BaseTenantEntity
{
    private static final long serialVersionUID = 1991299883735176990L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SSN_ID_SEQ")
    @SequenceGenerator(name = "SSN_ID_SEQ", sequenceName = "SSN_ID_SEQ", allocationSize = 100)
    @Column(name = "SSN_PK_ID", length = 11)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "SDE_PK_ID")
    private ScorecardScore scorecardScore;
    @Column(name = "SNAPSHOTDATE", length = 11)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public ScorecardScore getScorecardScore() {
        return this.scorecardScore;
    }
    
    public void setScorecardScore(final ScorecardScore scorecardScore) {
        this.scorecardScore = scorecardScore;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

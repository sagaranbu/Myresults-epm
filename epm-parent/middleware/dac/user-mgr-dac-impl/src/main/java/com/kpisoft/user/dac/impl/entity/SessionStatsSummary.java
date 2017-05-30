package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "USER_TYPE_SESSION_SUMMARY")
public class SessionStatsSummary extends BaseTenantEntity
{
    private static final long serialVersionUID = 9057982318198954481L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UTSS_ID_SEQ")
    @SequenceGenerator(name = "UTSS_ID_SEQ", sequenceName = "UTSS_ID_SEQ")
    @Column(name = "UTSS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "AGGREGATION_PERIOD", length = 11)
    private Integer agrPeriod;
    @Column(name = "USER_TYPE", length = 11)
    private Integer userType;
    @Column(name = "CURRENT_SESSION_COUNT", length = 11)
    private Integer currentSessionCnt;
    @Column(name = "PEAK_SESSION_COUNT", length = 11)
    private Integer peakSessionCnt;
    @Column(name = "TOTAL_SESSION_DURATION", length = 11)
    private Integer totalSessionDuration;
    @Column(name = "AVERAGE_SESSION_DURATION", length = 11)
    private Integer avgSessionDuration;
    @Column(name = "MAX_SESSION_DURATION", length = 11)
    private Integer maxSessionDuration;
    @Column(name = "MIN_SESSION_DURATION", length = 11)
    private Integer minSessionDuration;
    @Column(name = "TOTAL_SESSION_COUNT", length = 11)
    private Integer totalSessionCount;
    @Column(name = "CURRENT_USER_COUNT", length = 11)
    private Integer currentUserCnt;
    @Column(name = "PEAK_USER_COUNT", length = 11)
    private Integer peakUserCnt;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getAgrPeriod() {
        return this.agrPeriod;
    }
    
    public void setAgrPeriod(final Integer agrPeriod) {
        this.agrPeriod = agrPeriod;
    }
    
    public Integer getUserType() {
        return this.userType;
    }
    
    public void setUserType(final Integer userType) {
        this.userType = userType;
    }
    
    public Integer getCurrentSessionCnt() {
        return this.currentSessionCnt;
    }
    
    public void setCurrentSessionCnt(final Integer currentSessionCnt) {
        this.currentSessionCnt = currentSessionCnt;
    }
    
    public Integer getPeakSessionCnt() {
        return this.peakSessionCnt;
    }
    
    public void setPeakSessionCnt(final Integer peakSessionCnt) {
        this.peakSessionCnt = peakSessionCnt;
    }
    
    public Integer getTotalSessionDuration() {
        return this.totalSessionDuration;
    }
    
    public void setTotalSessionDuration(final Integer totalSessionDuration) {
        this.totalSessionDuration = totalSessionDuration;
    }
    
    public Integer getAvgSessionDuration() {
        return this.avgSessionDuration;
    }
    
    public void setAvgSessionDuration(final Integer avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }
    
    public Integer getMaxSessionDuration() {
        return this.maxSessionDuration;
    }
    
    public void setMaxSessionDuration(final Integer maxSessionDuration) {
        this.maxSessionDuration = maxSessionDuration;
    }
    
    public Integer getMinSessionDuration() {
        return this.minSessionDuration;
    }
    
    public void setMinSessionDuration(final Integer minSessionDuration) {
        this.minSessionDuration = minSessionDuration;
    }
    
    public Integer getTotalSessionCount() {
        return this.totalSessionCount;
    }
    
    public void setTotalSessionCount(final Integer totalSessionCount) {
        this.totalSessionCount = totalSessionCount;
    }
    
    public Integer getCurrentUserCnt() {
        return this.currentUserCnt;
    }
    
    public void setCurrentUserCnt(final Integer currentUserCnt) {
        this.currentUserCnt = currentUserCnt;
    }
    
    public Integer getPeakUserCnt() {
        return this.peakUserCnt;
    }
    
    public void setPeakUserCnt(final Integer peakUserCnt) {
        this.peakUserCnt = peakUserCnt;
    }
}

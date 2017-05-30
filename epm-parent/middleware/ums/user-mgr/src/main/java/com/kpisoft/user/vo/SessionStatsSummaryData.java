package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;

public class SessionStatsSummaryData extends BaseValueObject
{
    private Integer id;
    private Integer agrPeriod;
    private Integer userType;
    private int currentSessionCnt;
    private int peakSessionCnt;
    private int totalSessionDuration;
    private int avgSessionDuration;
    private int maxSessionDuration;
    private int minSessionDuration;
    private int totalSessionCount;
    private int currentUserCnt;
    private int peakUserCnt;
    
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
    
    public int getCurrentSessionCnt() {
        return this.currentSessionCnt;
    }
    
    public void setCurrentSessionCnt(final int currentSessionCnt) {
        this.currentSessionCnt = currentSessionCnt;
    }
    
    public int getPeakSessionCnt() {
        return this.peakSessionCnt;
    }
    
    public void setPeakSessionCnt(final int peakSessionCnt) {
        this.peakSessionCnt = peakSessionCnt;
    }
    
    public int getTotalSessionDuration() {
        return this.totalSessionDuration;
    }
    
    public void setTotalSessionDuration(final int totalSessionDuration) {
        this.totalSessionDuration = totalSessionDuration;
    }
    
    public int getAvgSessionDuration() {
        return this.avgSessionDuration;
    }
    
    public void setAvgSessionDuration(final int avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }
    
    public int getMaxSessionDuration() {
        return this.maxSessionDuration;
    }
    
    public void setMaxSessionDuration(final int maxSessionDuration) {
        this.maxSessionDuration = maxSessionDuration;
    }
    
    public int getMinSessionDuration() {
        return this.minSessionDuration;
    }
    
    public void setMinSessionDuration(final int minSessionDuration) {
        this.minSessionDuration = minSessionDuration;
    }
    
    public int getTotalSessionCount() {
        return this.totalSessionCount;
    }
    
    public void setTotalSessionCount(final int totalSessionCount) {
        this.totalSessionCount = totalSessionCount;
    }
    
    public int getCurrentUserCnt() {
        return this.currentUserCnt;
    }
    
    public void setCurrentUserCnt(final int currentUserCnt) {
        this.currentUserCnt = currentUserCnt;
    }
    
    public int getPeakUserCnt() {
        return this.peakUserCnt;
    }
    
    public void setPeakUserCnt(final int peakUserCnt) {
        this.peakUserCnt = peakUserCnt;
    }
}

package com.canopus.mw.dto;

import java.util.*;

public class DashboardMessageUserData extends BaseValueObject
{
    private static final long serialVersionUID = 7180767962842336517L;
    private Integer id;
    private Integer dashboardMessageId;
    private Integer recipientUserId;
    private boolean isDisabled;
    private String disabledBy;
    private Date disabledOn;
    private Date startDate;
    private Date endDate;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDashboardMessageId() {
        return this.dashboardMessageId;
    }
    
    public void setDashboardMessageId(final Integer dashboardMessageId) {
        this.dashboardMessageId = dashboardMessageId;
    }
    
    public Integer getRecipientUserId() {
        return this.recipientUserId;
    }
    
    public void setRecipientUserId(final Integer recipientUserId) {
        this.recipientUserId = recipientUserId;
    }
    
    public boolean isDisabled() {
        return this.isDisabled;
    }
    
    public void setDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public String getDisabledBy() {
        return this.disabledBy;
    }
    
    public void setDisabledBy(final String disabledBy) {
        this.disabledBy = disabledBy;
    }
    
    public Date getDisabledOn() {
        return this.disabledOn;
    }
    
    public void setDisabledOn(final Date disabledOn) {
        this.disabledOn = disabledOn;
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
}

package com.canopus.mw.dto;

import java.util.*;

public class DashboardMessageData extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer notificationId;
    private String message;
    private String subject;
    private Date messageTime;
    private String status;
    private boolean isCancellable;
    private String disabledBy;
    private Date disabledOn;
    private Date startDate;
    private Date endDate;
    private String customData;
    private List<Integer> recipientUserIds;
    private Integer reminderDays;
    
    public DashboardMessageData() {
        this.recipientUserIds = new ArrayList<Integer>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getNotificationId() {
        return this.notificationId;
    }
    
    public void setNotificationId(final Integer notificationId) {
        this.notificationId = notificationId;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(final String subject) {
        this.subject = subject;
    }
    
    public Date getMessageTime() {
        return this.messageTime;
    }
    
    public void setMessageTime(final Date messageTime) {
        this.messageTime = messageTime;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public boolean isCancellable() {
        return this.isCancellable;
    }
    
    public void setCancellable(final boolean isCancellable) {
        this.isCancellable = isCancellable;
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
    
    public List<Integer> getRecipientUserIds() {
        return this.recipientUserIds;
    }
    
    public void setRecipientUserIds(final List<Integer> recipientUserIds) {
        this.recipientUserIds = recipientUserIds;
    }
    
    public String getCustomData() {
        return this.customData;
    }
    
    public void setCustomData(final String customData) {
        this.customData = customData;
    }
    
    public Integer getReminderDays() {
        return this.reminderDays;
    }
    
    public void setReminderDays(final Integer reminderDays) {
        this.reminderDays = reminderDays;
    }
}

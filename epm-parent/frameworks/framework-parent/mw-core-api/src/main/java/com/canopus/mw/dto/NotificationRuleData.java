package com.canopus.mw.dto;

import java.util.*;

public class NotificationRuleData extends BaseValueObject
{
    private Integer id;
    private Integer eventId;
    private String eventHandlingMechanism;
    private String notificationChannel;
    private String messageTemplate;
    private String subjectTemplate;
    private boolean isDisabled;
    private List<NotificationSettingData> notificationSettings;
    private List<NotificationRecipientData> notificationRecipients;
    
    public NotificationRuleData() {
        this.notificationSettings = new ArrayList<NotificationSettingData>();
        this.notificationRecipients = new ArrayList<NotificationRecipientData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEventId() {
        return this.eventId;
    }
    
    public void setEventId(final Integer eventId) {
        this.eventId = eventId;
    }
    
    public String getEventHandlingMechanism() {
        return this.eventHandlingMechanism;
    }
    
    public void setEventHandlingMechanism(final String eventHandlingMechanism) {
        this.eventHandlingMechanism = eventHandlingMechanism;
    }
    
    public String getNotificationChannel() {
        return this.notificationChannel;
    }
    
    public void setNotificationChannel(final String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
    
    public String getMessageTemplate() {
        return this.messageTemplate;
    }
    
    public void setMessageTemplate(final String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
    
    public String getSubjectTemplate() {
        return this.subjectTemplate;
    }
    
    public void setSubjectTemplate(final String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }
    
    public boolean getIsDisabled() {
        return this.isDisabled;
    }
    
    public void setIsDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public List<NotificationSettingData> getNotificationSettings() {
        return this.notificationSettings;
    }
    
    public void setNotificationSettings(final List<NotificationSettingData> notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
    
    public List<NotificationRecipientData> getNotificationRecipients() {
        return this.notificationRecipients;
    }
    
    public void setNotificationRecipients(final List<NotificationRecipientData> notificationRecipients) {
        this.notificationRecipients = notificationRecipients;
    }
}

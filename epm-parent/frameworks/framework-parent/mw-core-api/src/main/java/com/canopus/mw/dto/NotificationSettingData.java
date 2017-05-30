package com.canopus.mw.dto;

public class NotificationSettingData
{
    private Integer id;
    private String settingValue;
    private Integer notificationId;
    private NotificationSettingDefinationData notificationSettingDef;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getSettingValue() {
        return this.settingValue;
    }
    
    public void setSettingValue(final String settingValue) {
        this.settingValue = settingValue;
    }
    
    public NotificationSettingDefinationData getNotificationSettingDef() {
        return this.notificationSettingDef;
    }
    
    public void setNotificationSettingDef(final NotificationSettingDefinationData notificationSettingDef) {
        this.notificationSettingDef = notificationSettingDef;
    }
    
    public Integer getNotificationId() {
        return this.notificationId;
    }
    
    public void setNotificationId(final Integer notificationId) {
        this.notificationId = notificationId;
    }
}

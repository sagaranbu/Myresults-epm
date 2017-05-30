package com.canopus.mw.dto;

public class NotificationRecipientData extends BaseValueObject
{
    private Integer id;
    private String recipientType;
    private String recipient;
    private Integer notificationId;
    private boolean isExclusion;
    private boolean isDigest;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getRecipientType() {
        return this.recipientType;
    }
    
    public void setRecipientType(final String recipientType) {
        this.recipientType = recipientType;
    }
    
    public String getRecipient() {
        return this.recipient;
    }
    
    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }
    
    public Integer getNotificationId() {
        return this.notificationId;
    }
    
    public void setNotificationId(final Integer notificationId) {
        this.notificationId = notificationId;
    }
    
    public boolean getIsExclusion() {
        return this.isExclusion;
    }
    
    public void setIsExclusion(final boolean isExclusion) {
        this.isExclusion = isExclusion;
    }
    
    public boolean getIsDigest() {
        return this.isDigest;
    }
    
    public void setIsDigest(final boolean isDigest) {
        this.isDigest = isDigest;
    }
}

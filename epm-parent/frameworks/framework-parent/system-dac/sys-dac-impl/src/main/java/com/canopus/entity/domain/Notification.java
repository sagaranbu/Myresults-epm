package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_MET_NOTIFICATION")
public class Notification extends BaseTenantEntity
{
    private static final long serialVersionUID = 4981255433688742846L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NOTIFICATION_ID_SEQ")
    @SequenceGenerator(name = "NOTIFICATION_ID_SEQ", sequenceName = "NOTIFICATION_ID_SEQ")
    @Column(name = "NOTIFICATION_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EVENT_FK_ID")
    private Integer eventId;
    @Column(name = "EVENT_HANDLING_MECHANISM")
    private String eventHandlingMechanism;
    @Column(name = "NOTIFICATION_CHANNEL")
    private String notificationChannel;
    @Column(name = "MESSAGE_TEMPLATE")
    private String messageTemplate;
    @Column(name = "SUBJECT_TEMPLATE")
    private String subjectTemplate;
    @Column(name = "IS_DISABLED")
    private boolean isDisabled;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NTF_FK_ID")
    private List<NotificationSetting> notificationSettings;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NOTIFICATION_FK_ID")
    private List<NotificationRecipient> notificationRecipients;
    
    public Notification() {
        this.notificationSettings = new ArrayList<NotificationSetting>();
        this.notificationRecipients = new ArrayList<NotificationRecipient>();
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
    
    public List<NotificationSetting> getNotificationSettings() {
        return this.notificationSettings;
    }
    
    public void setNotificationSettings(final List<NotificationSetting> notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
    
    public List<NotificationRecipient> getNotificationRecipients() {
        return this.notificationRecipients;
    }
    
    public void setNotificationRecipients(final List<NotificationRecipient> notificationRecipients) {
        this.notificationRecipients = notificationRecipients;
    }
}

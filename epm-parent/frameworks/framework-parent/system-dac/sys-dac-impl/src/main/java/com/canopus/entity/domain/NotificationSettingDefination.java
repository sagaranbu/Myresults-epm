package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_NTF_SETING_DEF")
public class NotificationSettingDefination extends BaseTenantEntity
{
    private static final long serialVersionUID = 7594902342535504037L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NTF_STING_DEF_ID_SEQ")
    @SequenceGenerator(name = "NTF_STING_DEF_ID_SEQ", sequenceName = "NTF_STING_DEF_ID_SEQ")
    @Column(name = "NTF_STING_DEF_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SETTING_TYPE", length = 50)
    private String settingType;
    @Column(name = "SETTING_DESCRIPTION", length = 1000)
    private String settingDescription;
    @Column(name = "SETTING_NAME", length = 100)
    private String settingName;
    @Column(name = "NOTIFICATION_CHANNEL")
    private String notificationChannel;
    @Column(name = "EVENT_HANDLING_MECHANISM")
    private String eventHandlingMechanism;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getSettingType() {
        return this.settingType;
    }
    
    public void setSettingType(final String settingType) {
        this.settingType = settingType;
    }
    
    public String getSettingDescription() {
        return this.settingDescription;
    }
    
    public void setSettingDescription(final String settingDescription) {
        this.settingDescription = settingDescription;
    }
    
    public String getSettingName() {
        return this.settingName;
    }
    
    public void setSettingName(final String settingName) {
        this.settingName = settingName;
    }
    
    public String getNotificationChannel() {
        return this.notificationChannel;
    }
    
    public void setNotificationChannel(final String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
    
    public String getEventHandlingMechanism() {
        return this.eventHandlingMechanism;
    }
    
    public void setEventHandlingMechanism(final String eventHandlingMechanism) {
        this.eventHandlingMechanism = eventHandlingMechanism;
    }
}

package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_NOTIFICATION_SETTING")
public class NotificationSetting extends BaseTenantEntity
{
    private static final long serialVersionUID = -90613390106578800L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NTF_STING_ID_SEQ")
    @SequenceGenerator(name = "NTF_STING_ID_SEQ", sequenceName = "NTF_STING_ID_SEQ")
    @Column(name = "NTF_STING_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SETTING_VALUE")
    private String settingValue;
    @Column(name = "NTF_FK_ID")
    private Integer notificationId;
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(referencedColumnName = "NTF_STING_DEF_PK_ID", name = "NTF_STING_DEF_FK_ID")
    private NotificationSettingDefination notificationSettingDef;
    
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
    
    public NotificationSettingDefination getNotificationSettingDef() {
        return this.notificationSettingDef;
    }
    
    public void setNotificationSettingDef(final NotificationSettingDefination notificationSettingDef) {
        this.notificationSettingDef = notificationSettingDef;
    }
    
    public Integer getNotificationId() {
        return this.notificationId;
    }
    
    public void setNotificationId(final Integer notificationId) {
        this.notificationId = notificationId;
    }
}

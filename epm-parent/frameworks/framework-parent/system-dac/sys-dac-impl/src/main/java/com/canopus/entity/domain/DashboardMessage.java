package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_MET_DASHBOARDMSG")
public class DashboardMessage extends BaseTenantEntity
{
    private static final long serialVersionUID = -1815113572931143207L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DASHBOARDMSG_ID_SEQ")
    @SequenceGenerator(name = "DASHBOARDMSG_ID_SEQ", sequenceName = "DASHBOARDMSG_ID_SEQ")
    @Column(name = "DASHBOARDMSG_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NOTIFICATION_FK_ID")
    private Integer notificationId;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "MESSAGE_TIME")
    private Date messageTime;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "IS_CANCELLABLE")
    private boolean isCancellable;
    @Column(name = "DISABLED_BY")
    private String disabledBy;
    @Column(name = "DISABLED_ON")
    private Date disabledOn;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "CUSTOM_DATA", length = 4000)
    private String customData;
    @Column(name = "REMINDER_DAYS", length = 11)
    private Integer reminderDays;
    
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
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
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

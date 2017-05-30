package com.canopus.entity.domain;

import com.canopus.dac.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_EVENT")
public class MiddlewareEventData extends BaseTenantEntity
{
    private static final long serialVersionUID = -1804372397428879535L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MW_EVENT_ID_SEQ")
    @SequenceGenerator(name = "MW_EVENT_ID_SEQ", sequenceName = "MW_EVENT_ID_SEQ")
    @Column(name = "CME_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EVENT_TYPE", length = 100)
    private String eventType;
    @Column(name = "IS_PROCESSED")
    private Boolean isProcessed;
    @Column(name = "SCHEDULE_DATE")
    private Date scheduleDate;
    @Column(name = "EVENT_DATA")
    @Lob
    private byte[] eventData;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    public Boolean getIsProcessed() {
        return this.isProcessed;
    }
    
    public void setIsProcessed(final Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }
    
    public Date getScheduleDate() {
        return this.scheduleDate;
    }
    
    public void setScheduleDate(final Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    
    public byte[] getEventData() {
        return this.eventData;
    }
    
    public void setEventData(final byte[] eventData) {
        this.eventData = eventData;
    }
}

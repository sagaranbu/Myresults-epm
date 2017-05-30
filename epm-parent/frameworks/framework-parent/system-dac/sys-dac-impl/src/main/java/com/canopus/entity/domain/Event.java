package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_MET_EVENT")
public class Event extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 2976194259225396373L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EVENT_ID_SEQ")
    @SequenceGenerator(name = "EVENT_ID_SEQ", sequenceName = "EVENT_ID_SEQ")
    @Column(name = "EVENT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EVENT_ID")
    private String eventId;
    @Column(name = "EVENT_ORIGIN_MECHANISM")
    private String eventOriginMechanism;
    @Column(name = "EVENT_DESCRIPTION")
    private String eventDescription;
    @Column(name = "IS_SYSTEM_EVENT")
    private boolean isSystemEvent;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "EVENT_FK_ID")
    private List<EventVariable> eventVariables;
    
    public Event() {
        this.eventVariables = new ArrayList<EventVariable>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getEventId() {
        return this.eventId;
    }
    
    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }
    
    public String getEventOriginMechanism() {
        return this.eventOriginMechanism;
    }
    
    public void setEventOriginMechanism(final String eventOriginMechanism) {
        this.eventOriginMechanism = eventOriginMechanism;
    }
    
    public String getEventDescription() {
        return this.eventDescription;
    }
    
    public void setEventDescription(final String eventDescription) {
        this.eventDescription = eventDescription;
    }
    
    public boolean isSystemEvent() {
        return this.isSystemEvent;
    }
    
    public void setSystemEvent(final boolean isSystemEvent) {
        this.isSystemEvent = isSystemEvent;
    }
    
    public List<EventVariable> getEventVariables() {
        return this.eventVariables;
    }
    
    public void setEventVariables(final List<EventVariable> eventVariables) {
        this.eventVariables = eventVariables;
    }
}

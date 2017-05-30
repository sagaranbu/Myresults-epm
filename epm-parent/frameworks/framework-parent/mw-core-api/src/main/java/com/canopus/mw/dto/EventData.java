package com.canopus.mw.dto;

import java.util.*;

public class EventData extends BaseValueObject
{
    private Integer id;
    private String eventId;
    private String eventOriginMechanism;
    private String eventDescription;
    private boolean isSystemEvent;
    private List<EventVariableData> eventVariables;
    
    public EventData() {
        this.eventVariables = new ArrayList<EventVariableData>();
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
    
    public List<EventVariableData> getEventVariables() {
        return this.eventVariables;
    }
    
    public void setEventVariables(final List<EventVariableData> eventVariables) {
        this.eventVariables = eventVariables;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final EventData other = (EventData)obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "EventData [" + ((this.id != null) ? ("id=" + this.id + ", ") : "") + ((this.eventId != null) ? ("eventId=" + this.eventId + ", ") : "") + ((this.eventOriginMechanism != null) ? ("eventOriginMechanism=" + this.eventOriginMechanism + ", ") : "") + ((this.eventDescription != null) ? ("eventDescription=" + this.eventDescription + ", ") : "") + "isSystemEvent=" + this.isSystemEvent + ", " + ((this.eventVariables != null) ? ("eventVariables=" + this.eventVariables) : "") + "]";
    }
}

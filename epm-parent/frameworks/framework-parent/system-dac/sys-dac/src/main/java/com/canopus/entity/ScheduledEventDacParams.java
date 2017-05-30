package com.canopus.entity;

import com.canopus.mw.dto.param.*;

public enum ScheduledEventDacParams implements IMiddlewareParam
{
    MIDDLEWARE_EVENT, 
    MIDDLEWARE_EVENT_ID, 
    EVENTS_TO_FIRE, 
    MIDDLEWARE_EVENT_PROCESSED;
    
    public String getParamName() {
        return this.name();
    }
}

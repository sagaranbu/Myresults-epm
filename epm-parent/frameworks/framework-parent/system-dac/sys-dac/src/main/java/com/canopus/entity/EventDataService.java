package com.canopus.entity;

import com.canopus.mw.dto.*;

public interface EventDataService
{
    public static final String ERR_EVENT_VARIABLE_INVALID_DATA = "evnt-var-err-000";
    public static final String ERR_EVENTS_UNKNOWN_EXCEPTION = "evnt-err-001";
    public static final String ERR_EVENT_INVALID_DATA = "evnt-err-002";
    
    Response saveEventVariable(final Request p0);
    
    Response saveEvent(final Request p0);
    
    Response getAllEvents(final Request p0);
}

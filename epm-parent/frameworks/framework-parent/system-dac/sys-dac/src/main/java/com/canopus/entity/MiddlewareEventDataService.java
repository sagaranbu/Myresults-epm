package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface MiddlewareEventDataService extends DataAccessService
{
    public static final String ERR_ENTITY_UNKNOWN_EXCEPTION = "MIDDLEWARE-EVENT-000";
    public static final String INVALID_DATA = "MIDDLEWARE-EVENT-100";
    
    Response addScheduledEvent(final Request p0);
    
    Response getEventsToFire(final Request p0);
    
    Response setProcessed(final Request p0);
}

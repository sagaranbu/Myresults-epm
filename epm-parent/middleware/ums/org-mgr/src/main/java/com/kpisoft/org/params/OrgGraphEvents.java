package com.kpisoft.org.params;

import com.canopus.mw.events.*;

public enum OrgGraphEvents implements IMiddlewareEventType
{
    SUSPEND_ORG_REL_EVENT, 
    SUSPEND_ORG_EVENT;
    
    public String getEventId() {
        return this.getClass().getName() + "." + this.name();
    }
}

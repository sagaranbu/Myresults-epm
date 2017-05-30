package com.canopus.mw.sys;

import com.canopus.mw.dto.param.*;

public enum ScheduledEventParams implements IMiddlewareParam
{
    MIDDLEWARE_EVENT;
    
    public String getParamName() {
        return this.name();
    }
}

package com.canopus.mw.dto.param;

public enum EventParam implements IMiddlewareParam
{
    EVENTDATA, 
    EVENTID, 
    EVENTLIST;
    
    @Override
    public String getParamName() {
        return this.name();
    }
}

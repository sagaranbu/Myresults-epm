package com.canopus.mw.events;

public interface IMiddlewareEventClient
{
    void fireEvent(final MiddlewareEvent p0);
    
    void sendToScheduler(final MiddlewareEvent p0);
    
    void fireEvent1(final MiddlewareEvent p0);
    
    void fireEvent2(final MiddlewareEvent p0);
    
    Object getFromService(final String p0, final String p1, final String p2, final MiddlewareEvent p3);
    
    void initEventData();
}

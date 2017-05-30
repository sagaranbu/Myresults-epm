package com.canopus.event.mgr;

import com.canopus.event.mgr.vo.*;
import com.canopus.mw.events.*;
import com.canopus.mw.dto.*;

public interface IEventListenerInvokationHandler
{
    String getType();
    
    String invokeMechanism();
    
    EventOriginData getEventOriginData();
    
    String invoke(final MiddlewareEvent p0);
    
    String invoke(final MiddlewareEventBean p0);
    
    EventData getEventData();
}

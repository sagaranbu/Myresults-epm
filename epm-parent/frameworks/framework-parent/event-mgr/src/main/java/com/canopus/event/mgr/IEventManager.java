package com.canopus.event.mgr;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface IEventManager extends MiddlewareService
{
    Response fireEvent(final Request p0);
    
    Response getMWServiceEventOriginators(final Request p0);
    
    Response getMWServiceEvents(final Request p0);
    
    Response refreshNotificationCache(final Request p0);
}

package com.canopus.mw;

import com.canopus.mw.dto.*;

public interface IMiddlewareQueryService extends MiddlewareService
{
    Response getData(final Request p0);
    
    Response consumeEvent(final Request p0);
}

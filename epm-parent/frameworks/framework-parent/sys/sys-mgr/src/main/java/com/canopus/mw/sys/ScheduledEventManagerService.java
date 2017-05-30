package com.canopus.mw.sys;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface ScheduledEventManagerService extends MiddlewareService
{
    Response scheduleEvent(final Request p0);
}

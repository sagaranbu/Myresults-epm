package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface SessionStatisticsManagerService extends MiddlewareService
{
    Response getSessionSummary(final Request p0);
    
    Response getUserLastSessionDetails(final Request p0);
}

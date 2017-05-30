package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface SessionStatsSummaryDataService extends DataAccessService
{
    Response getSessionSummary(final Request p0);
    
    Response saveSessionSummary(final Request p0);
}

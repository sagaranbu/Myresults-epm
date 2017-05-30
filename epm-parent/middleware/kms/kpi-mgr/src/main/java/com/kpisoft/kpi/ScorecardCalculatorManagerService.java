package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface ScorecardCalculatorManagerService extends MiddlewareService
{
    Response calcScorecardRatingLevel(final Request p0);
    
    Response calcGroupAcievement(final Request p0);
    
    Response calcScorecardAcievement(final Request p0);
}

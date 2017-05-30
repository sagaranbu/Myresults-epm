package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiCalculatorManagerService extends MiddlewareService
{
    Response calcKpiRatingLevel(final Request p0);
    
    Response calcKpiAcievement(final Request p0);
    
    Response calcKpiAggregatedTarget(final Request p0);
    
    Response calculateKpiPerformancePoints(final Request p0);
}

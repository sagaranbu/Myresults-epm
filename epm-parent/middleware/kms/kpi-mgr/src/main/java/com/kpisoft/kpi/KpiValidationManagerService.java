package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiValidationManagerService extends MiddlewareService
{
    Response validateKpi(final Request p0);
    
    Response validateScorecard(final Request p0);
    
    Response addCustomValidator(final Request p0);
}

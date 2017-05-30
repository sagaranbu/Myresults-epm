package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiCategoryManagerService extends MiddlewareService
{
    Response getKpiCategory(final Request p0);
    
    Response createKpiCategory(final Request p0);
    
    Response updateKpiCategory(final Request p0);
}

package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiOrgTypeManagerService extends MiddlewareService
{
    Response getKpiOrgType(final Request p0);
    
    Response createKpiOrgType(final Request p0);
    
    Response updateKpiOrgType(final Request p0);
}

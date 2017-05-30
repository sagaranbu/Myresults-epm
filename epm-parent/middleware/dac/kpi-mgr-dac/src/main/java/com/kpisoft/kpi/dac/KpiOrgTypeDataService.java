package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface KpiOrgTypeDataService extends DataAccessService
{
    Response getKpiOrgType(final Request p0);
    
    Response saveKpiOrgType(final Request p0);
    
    Response deleteKpiOrgType(final Request p0);
}

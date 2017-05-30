package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface KpiCategoryDataService extends DataAccessService
{
    Response getKpiCategory(final Request p0);
    
    Response saveKpiCategory(final Request p0);
    
    Response deleteKpiCategory(final Request p0);
}

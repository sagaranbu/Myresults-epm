package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiTargetManagerService extends MiddlewareService
{
    Response createKpiTarget(final Request p0);
    
    Response getKpiTarget(final Request p0);
    
    Response updateKpiTarget(final Request p0);
    
    Response removeKpiTarget(final Request p0);
    
    Response createScaleValue(final Request p0);
    
    Response getScaleValue(final Request p0);
    
    Response updateScaleValue(final Request p0);
    
    Response removeScaleValue(final Request p0);
    
    Response createMasterScale(final Request p0);
    
    Response getMasterScale(final Request p0);
    
    Response updateMasterScale(final Request p0);
    
    Response removeMasterScale(final Request p0);
    
    Response getAllMasterScales(final Request p0);
    
    Response getScaleValueForTarget(final Request p0);
    
    Response getKpiTargetForKpi(final Request p0);
    
    Response searchKpiReviewFrequency(final Request p0);
}

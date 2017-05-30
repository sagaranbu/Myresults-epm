package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;
import org.springframework.transaction.annotation.*;

public interface KpiTargetDataService extends DataAccessService
{
    Response saveKpiTarget(final Request p0);
    
    Response getKpiTarget(final Request p0);
    
    Response updateKpiTarget(final Request p0);
    
    Response deleteKpiTarget(final Request p0);
    
    Response createScaleValue(final Request p0);
    
    Response getScaleValue(final Request p0);
    
    Response updateScaleValue(final Request p0);
    
    Response removeScaleValue(final Request p0);
    
    Response createMasterScale(final Request p0);
    
    Response getMasterScale(final Request p0);
    
    Response updateMasterScale(final Request p0);
    
    Response removeMasterScale(final Request p0);
    
    @Transactional
    Response getAllMasterScales(final Request p0);
    
    Response getScaleValueForTarget(final Request p0);
    
    Response getKpiTargetForKpi(final Request p0);
    
    Response searchKpiReviewFrequency(final Request p0);
}

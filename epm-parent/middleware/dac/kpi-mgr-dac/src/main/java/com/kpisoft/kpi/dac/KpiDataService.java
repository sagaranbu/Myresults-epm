package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface KpiDataService extends DataAccessService
{
    Response getKpi(final Request p0);
    
    Response saveKpi(final Request p0);
    
    Response saveScoreDetailsInKpiForMultiple(final Request p0);
    
    Response updateKpiDetailsForMultiple(final Request p0);
    
    Response deleteKpi(final Request p0);
    
    Response getKpiCountByScoreCard(final Request p0);
    
    Response getKpisForEmployee(final Request p0);
    
    Response getKpisByStrategyNode(final Request p0);
    
    Response validateKpiWithRootcode(final Request p0);
    
    Response getKpiUom(final Request p0);
    
    Response saveKpiUom(final Request p0);
    
    Response deleteKpiUom(final Request p0);
    
    Response getAllKpiUom(final Request p0);
    
    Response getKpiType(final Request p0);
    
    Response saveKpiType(final Request p0);
    
    Response deleteKpiType(final Request p0);
    
    Response getAllKpiType(final Request p0);
    
    Response getKpiAggregationTypeById(final Request p0);
    
    Response getAllKpiAggregationTypes(final Request p0);
    
    Response saveKpiScore(final Request p0);
    
    Response saveKpiScoreForMultiple(final Request p0);
    
    Response getKpiScoreById(final Request p0);
    
    Response getAllPeriodTypes(final Request p0);
    
    Response getAllPeriodMsaters(final Request p0);
    
    Response getPositionKpiEmployeeRelations(final Request p0);
    
    Response searchKpi(final Request p0);
    
    Response searchKpiScore(final Request p0);
    
    Response getPeriodMasterByPeriodType(final Request p0);
    
    Response getKpiKpiRelationships(final Request p0);
    
    Response getAllKpiRelationships(final Request p0);
    
    Response getKpiRelationshipTypeNameById(final Request p0);
    
    Response searchKpiTag(final Request p0);
    
    Response getKpisByScorecardIdAndKpiTagId(final Request p0);
    
    Response searchPeriodType(final Request p0);
    
    Response searchPeriodMaster(final Request p0);
    
    Response deleteKpis(final Request p0);
    
    Response getKpisByIds(final Request p0);
    
    Response updateKPIState(final Request p0);
    
    Response getKpiDataToCalcScore(final Request p0);
    
    Response searchKpiAggregationType(final Request p0);
    
    Response searchKpiUom(final Request p0);
    
    Response searchKpiType(final Request p0);
    
    Response getAllKpiScoreByKpiIds(final Request p0);
    
    Response getKpisByScorecardId(final Request p0);
    
    Response getKpiDataToCalcScorecardScore(final Request p0);
    
    Response deactivateKpis(final Request p0);
    
    Response getKpisByCount(final Request p0);
}

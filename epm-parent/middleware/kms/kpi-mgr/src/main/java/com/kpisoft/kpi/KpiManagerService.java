package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface KpiManagerService extends MiddlewareService
{
    Response getKpi(final Request p0);
    
    Response createKpi(final Request p0);
    
    Response updateKpi(final Request p0);
    
    Response updateKpiDetailsForMultiple(final Request p0);
    
    Response deleteKpi(final Request p0);
    
    Response getKpiCountByScoreCard(final Request p0);
    
    Response getKpisForEmployee(final Request p0);
    
    Response getKpisByStrategyNode(final Request p0);
    
    Response validateKpiWithRootcode(final Request p0);
    
    Response getKpiUom(final Request p0);
    
    Response getAllKpiUom(final Request p0);
    
    Response createKpiUom(final Request p0);
    
    Response updateKpiUom(final Request p0);
    
    Response deleteKpiUom(final Request p0);
    
    Response getKpiType(final Request p0);
    
    Response getAllKpiType(final Request p0);
    
    Response createKpiType(final Request p0);
    
    Response updateKpiType(final Request p0);
    
    Response deleteKpiType(final Request p0);
    
    Response getKpiAggregationTypeById(final Request p0);
    
    Response getAllKpiAggregationTypes(final Request p0);
    
    Response saveKpiScore(final Request p0);
    
    Response updateKpiScore(final Request p0);
    
    Response getKpiScoreById(final Request p0);
    
    Response getAllPeriodTypes(final Request p0);
    
    Response getPositionKpiEmployeeRelations(final Request p0);
    
    Response searchKpi(final Request p0);
    
    Response searchKpiScore(final Request p0);
    
    Response getPeriodMasterByPeriodType(final Request p0);
    
    Response searchKpiTag(final Request p0);
    
    Response searchPeriodType(final Request p0);
    
    Response getKpiRelationshipTypeNameById(final Request p0);
    
    Response getKpisByScorecardIdAndKpiTagId(final Request p0);
    
    Response searchPeriodMaster(final Request p0);
    
    Response getKpiKpiRelationships(final Request p0);
    
    Response getChildren(final Request p0);
    
    Response getChildrenByType(final Request p0);
    
    Response getParents(final Request p0);
    
    Response getParentsByType(final Request p0);
    
    Response getAscendants(final Request p0);
    
    Response getAscendantsByType(final Request p0);
    
    Response getAscendantsByIdList(final Request p0);
    
    Response getAscendantsByIdListAndType(final Request p0);
    
    Response getAscendantsGraph(final Request p0);
    
    Response getAscendantsGraphByType(final Request p0);
    
    Response getAscendantsGraphByIdList(final Request p0);
    
    Response getAscendantsGraphByIdListAndType(final Request p0);
    
    Response getDescendants(final Request p0);
    
    Response getDescendantsByType(final Request p0);
    
    Response getDescendantsByIdList(final Request p0);
    
    Response getDescendantsByIdListAndType(final Request p0);
    
    Response getDescendantsGraph(final Request p0);
    
    Response getDescendantsGraphByType(final Request p0);
    
    Response getDescendantsGraphByIdList(final Request p0);
    
    Response getDescendantsGraphByIdListAndType(final Request p0);
    
    Response getKpisByIds(final Request p0);
    
    Response updateKPIState(final Request p0);
    
    Response getKpiDataToCalcScore(final Request p0);
    
    Response searchKpiAggregationType(final Request p0);
    
    Response searchKpiUom(final Request p0);
    
    Response searchKpiType(final Request p0);
    
    Response getAllKpiScoreByKpiIds(final Request p0);
    
    Response getKpisByScorecardId(final Request p0);
    
    Response getKpiDataToCalcScorecardScore(final Request p0);
    
    Response getPeriodMasterById(final Request p0);
    
    Response deactivateKpi(final Request p0);
    
    Response getCurrentPeriodScore(final Request p0);
    
    Response refreshPerformanceDataCache(final Request p0);
    
    Response refreshKpiCacheandGraph(final Request p0);
    
    Response updateKpiGraph(final Request p0);
    
    Response reloadKpiKpiReationshipGraph(final Request p0);
}

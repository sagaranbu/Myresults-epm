package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface ScorecardDataService extends DataAccessService
{
    Response saveScorecard(final Request p0);
    
    Response getScorecard(final Request p0);
    
    Response updateScorecard(final Request p0);
    
    BooleanResponse deleteScorecard(final Request p0);
    
    Response searchScorecard(final Request p0);
    
    Response saveScorecardScore(final Request p0);
    
    Response getScorecardScore(final Request p0);
    
    Response searchScorecardScore(final Request p0);
    
    Response takeSnapshot(final Request p0);
    
    Response getSnapshot(final Request p0);
    
    Response updateScorecardStatus(final Request p0);
    
    Response updateScorecardScoreStatus(final Request p0);
    
    Response getAllScorecards(final Request p0);
}

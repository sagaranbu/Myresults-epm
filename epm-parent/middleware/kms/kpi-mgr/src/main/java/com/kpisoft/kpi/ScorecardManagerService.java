package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface ScorecardManagerService extends MiddlewareService
{
    Response createScorecard(final Request p0);
    
    Response getScorecard(final Request p0);
    
    Response updateScorecard(final Request p0);
    
    Response removeScorecard(final Request p0);
    
    Response searchScorecard(final Request p0);
    
    Response saveScorecardScore(final Request p0);
    
    Response updateScorecardScore(final Request p0);
    
    Response getScorecardScore(final Request p0);
    
    Response searchScorecardScore(final Request p0);
    
    Response takeSnapshot(final Request p0);
    
    Response getSnapshot(final Request p0);
    
    Response updateScorecardStatus(final Request p0);
    
    Response updateScorecardScoreStatus(final Request p0);
}

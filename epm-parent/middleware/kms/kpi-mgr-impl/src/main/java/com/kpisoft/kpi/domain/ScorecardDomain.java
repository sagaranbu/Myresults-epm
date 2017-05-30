package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.dac.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;

public class ScorecardDomain extends BaseDomainObject
{
    private ScorecardBean scorecardBean;
    private ScorecardDataService dataService;
    
    public ScorecardDomain(final ScorecardManager scorecardManager, final ScorecardDataService dataService) {
        this.dataService = dataService;
    }
    
    public ScorecardBean getScorecardBean() {
        return this.scorecardBean;
    }
    
    public void setScorecardBean(final ScorecardBean scorecardBean) {
        this.scorecardBean = scorecardBean;
    }
    
    public ScorecardBean createScorecard(ScorecardBean scorecardBean) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
        final Response response = this.dataService.saveScorecard(request);
        scorecardBean = (ScorecardBean)response.get(KpiParams.SCORECARD.name());
        return scorecardBean;
    }
    
    public Response getScorecard(final Request req) {
        return this.dataService.getScorecard(req);
    }
    
    public Response updateScorecard(final Request req) {
        return this.dataService.updateScorecard(req);
    }
    
    public BooleanResponse removeScorecard(final Request req) {
        return this.dataService.deleteScorecard(req);
    }
}

package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;

@Component
public class DefaultScorecardCalculatorManager extends BaseDomainManager
{
    public Double calcScorecardRatingLevel(final List<KpiData> kpiDataList) {
        final DefaultScorecardCalculatorDomain defaultScorecardcalcDomain = new DefaultScorecardCalculatorDomain();
        return defaultScorecardcalcDomain.calcScorecardRatingLevel(kpiDataList);
    }
    
    public Double calcScorecardAcievement(final List<ScorecardCalculatorUtility> scorecardUtilityList) {
        final DefaultScorecardCalculatorDomain defaultScorecardcalcDomain = new DefaultScorecardCalculatorDomain();
        return defaultScorecardcalcDomain.calcScorecardAcievement(scorecardUtilityList);
    }
    
    public Double calcGroupAcievement(final List<ScorecardCalculatorUtility> kpiDataList) {
        final DefaultScorecardCalculatorDomain defaultScorecardcalcDomain = new DefaultScorecardCalculatorDomain();
        return defaultScorecardcalcDomain.calcGroupAcievement(kpiDataList);
    }
}

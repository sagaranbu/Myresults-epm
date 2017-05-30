package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;

@Component
public class DefaultKpiCalculatorManager extends BaseDomainManager
{
    public Integer calcKpiRatingLevel(final String achievementType, final String kpiType, final double scoreActual, final KpiScaleBean kpiScaleBean, final Date dateScoreActual) {
        final DefaultKpiCalculatorDomain defaultKpicalcDomain = new DefaultKpiCalculatorDomain();
        return defaultKpicalcDomain.calcKpiRatingLevel(achievementType, kpiType, scoreActual, kpiScaleBean, dateScoreActual);
    }
    
    public Double calcKpiAggregatedTarget(final String aggregationType, final List<KpiTargetDataBean> kpiTargetDataBeanList) {
        final DefaultKpiCalculatorDomain defaultKpicalcDomain = new DefaultKpiCalculatorDomain();
        return defaultKpicalcDomain.calcKpiAggregatedTarget(aggregationType, kpiTargetDataBeanList);
    }
    
    public Double calcKpiAcievement(final KpiScoreBean kpiScoreBean, final String acievementType) {
        final DefaultKpiCalculatorDomain defaultKpicalcDomain = new DefaultKpiCalculatorDomain();
        return defaultKpicalcDomain.calcKpiAcievement(kpiScoreBean, acievementType);
    }
    
    public KpiCalcUtility calculateKpiPerformancePoints(final String achievementType, final String kpiType, final double scoreActual, final KpiScaleBean kpiScaleBean, final double target, final double targetCumulative, final Date dateScoreActual, final Date dateTarget) throws Exception {
        final DefaultKpiCalculatorDomain calcDomain = new DefaultKpiCalculatorDomain();
        return calcDomain.calculateKpiPerformancePoints(achievementType, kpiType, scoreActual, kpiScaleBean, target, targetCumulative, dateScoreActual, dateTarget);
    }
}

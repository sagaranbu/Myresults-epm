package com.kpisoft.strategy.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface StrategyTemplateDataService extends DataAccessService
{
    Response saveStrategyTemplate(final Request p0);
    
    Response getStrategyTemplate(final Request p0);
    
    Response deleteStrategyTemplate(final Request p0);
    
    Response getAllStrategyTemplate(final Request p0);
    
    Response createStrategyMap(final Request p0);
    
    Response getStrategyMap(final Request p0);
    
    Response deleteStrategyMap(final Request p0);
    
    Response getAllStrategyMap(final Request p0);
    
    Response searchStrategyMap(final Request p0);
}

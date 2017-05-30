package com.kpisoft.strategy;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface StrategyTemplateManagerService extends MiddlewareService
{
    Response createStrategyTemplate(final Request p0);
    
    Response getStrategyTemplate(final Request p0);
    
    Response getAllStrategyTemplate(final Request p0);
    
    Response getAllStrategyMap(final Request p0);
    
    Response deleteStrategyTemplate(final Request p0);
    
    Response createStrategyMap(final Request p0);
    
    Response getStrategyMap(final Request p0);
    
    Response deleteStrategyMap(final Request p0);
    
    Response searchStrategyMap(final Request p0);
}

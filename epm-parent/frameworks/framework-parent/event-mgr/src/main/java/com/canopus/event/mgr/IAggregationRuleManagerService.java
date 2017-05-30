package com.canopus.event.mgr;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface IAggregationRuleManagerService extends MiddlewareService
{
    Response getAggregationRule(final Request p0);
    
    Response saveAggregationRule(final Request p0);
}

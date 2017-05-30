package com.canopus.mw.aggregation;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface IAggregationService extends MiddlewareService
{
    Response aggregate(final Request p0);
    
    Response addSummaryDataService(final Request p0);
}

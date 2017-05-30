package com.kpisoft.strategy.profram.dac;

import com.canopus.mw.dto.*;
import com.canopus.dac.*;

public interface ReviewProcessTemplateDataService extends DataAccessService
{
    Response getReviewTemplate(final Request p0) throws DataAccessException, Exception;
}

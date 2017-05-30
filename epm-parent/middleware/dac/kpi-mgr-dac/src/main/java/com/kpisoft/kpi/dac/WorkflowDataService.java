package com.kpisoft.kpi.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface WorkflowDataService extends DataAccessService
{
    Response saveGoalSettingComments(final Request p0);
    
    Response getGoalSettingCommentsById(final Request p0);
    
    Response searchGoalSettingComments(final Request p0);
    
    Response savePerformanceComments(final Request p0);
    
    Response getPerformanceCommentsById(final Request p0);
    
    Response searchPerformanceComments(final Request p0);
    
    Response saveAllGoalSettingComments(final Request p0);
    
    Response saveAllPerformanceReviewComments(final Request p0);
}

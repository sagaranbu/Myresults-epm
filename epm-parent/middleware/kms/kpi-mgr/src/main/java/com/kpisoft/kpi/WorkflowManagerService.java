package com.kpisoft.kpi;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface WorkflowManagerService extends MiddlewareService
{
    Response goalSettingTrigger(final Request p0);
    
    Response scorecardSubmitTrigger(final Request p0);
    
    Response getWorkflowTaskList(final Request p0);
    
    Response getWorkflowTaskListByEntityId(final Request p0);
    
    Response goalSettingReviewOrApproveTrigger(final Request p0);
    
    Response scoreSubmitTrigger(final Request p0);
    
    Response performanceReviewOrApproveTrigger(final Request p0);
    
    Response saveGoalSettingComments(final Request p0);
    
    Response updateGoalSettingComments(final Request p0);
    
    Response getGoalSettingCommentsById(final Request p0);
    
    Response searchGoalSettingComments(final Request p0);
    
    Response savePerformanceComments(final Request p0);
    
    Response updatePerformanceComments(final Request p0);
    
    Response getPerformanceCommentsById(final Request p0);
    
    Response searchPerformanceComments(final Request p0);
    
    Response saveAllGoalSettingComments(final Request p0);
    
    Response saveAllPerformanceReviewComments(final Request p0);
    
    Response scorecardSubmitTriggerForMultiple(final Request p0);
    
    Response goalSettingReviewOrApproveTriggerMultiple(final Request p0);
}

package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.kpisoft.kpi.dac.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mwworkflow.*;
import org.activiti.bpmn.model.*;
import com.kpisoft.kpi.vo.param.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.kpisoft.kpi.vo.*;

@Component
public class WorkflowDomainManager extends BaseDomainManager
{
    @Autowired
    private WorkflowDataService dataService;
    
    public WorkflowDomainManager() {
        this.dataService = null;
    }
    
    public BpmnModel defineWorkflowModel(final BaseValueObjectMap workflowLeveles, final Integer entityId, final WorkflowType workflowType, final String activityWorkflowType) {
        final WorkflowDomain workflowDomain = new WorkflowDomain();
        return workflowDomain.defineWorkflowModel(workflowLeveles, entityId, workflowType, activityWorkflowType);
    }
    
    public Integer saveGoalSettingComments(final GoalSettingWorkflowCommentsBean data) {
        final Request request = new Request();
        request.put(WorkflowParams.GOAL_COMMENTS_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.saveGoalSettingComments(request);
        final Identifier identifier = (Identifier)response.get(WorkflowParams.GOAL_COMMENTS_ID.name());
        return identifier.getId();
    }
    
    public GoalSettingWorkflowCommentsBean getGoalSettingCommentsById(final Integer id) {
        final Request request = new Request();
        request.put(WorkflowParams.GOAL_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.getGoalSettingCommentsById(request);
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)response.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        return data;
    }
    
    public List<GoalSettingWorkflowCommentsBean> searchGoalSettingComments(final GoalSettingWorkflowCommentsBean data) {
        final Request request = new Request();
        request.put(WorkflowParams.GOAL_COMMENTS_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.searchGoalSettingComments(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final List<GoalSettingWorkflowCommentsBean> result = (List<GoalSettingWorkflowCommentsBean>)list.getValueObjectList();
        return result;
    }
    
    public Integer savePerformanceComments(final PerformanceReviewCommentsBean data) {
        final Request request = new Request();
        request.put(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.savePerformanceComments(request);
        final Identifier identifier = (Identifier)response.get(WorkflowParams.PERFORMANCE_COMMENTS_ID.name());
        return identifier.getId();
    }
    
    public PerformanceReviewCommentsBean getPerformanceCommentsById(final Integer id) {
        final Request request = new Request();
        request.put(WorkflowParams.PERFORMANCE_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.getPerformanceCommentsById(request);
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)response.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        return data;
    }
    
    public List<PerformanceReviewCommentsBean> searchPerformanceComments(final PerformanceReviewCommentsBean data) {
        final Request request = new Request();
        request.put(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.searchPerformanceComments(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name());
        final List<PerformanceReviewCommentsBean> result = (List<PerformanceReviewCommentsBean>)list.getValueObjectList();
        return result;
    }
    
    public Integer saveAllGoalSettingComments(final List<GoalSettingWorkflowCommentsBean> data) {
        final Request request = new Request();
        final BaseValueObjectList objectList = new BaseValueObjectList();
        objectList.setValueObjectList((List)data);
        request.put(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
        final Response response = this.dataService.saveAllGoalSettingComments(request);
        final Identifier id = (Identifier)response.get(WorkflowParams.COUNT.name());
        if (id != null) {
            return id.getId();
        }
        return 0;
    }
    
    public Integer saveAllPerformanceReviewComments(final List<PerformanceReviewCommentsBean> data) {
        final Request request = new Request();
        final BaseValueObjectList objectList = new BaseValueObjectList();
        objectList.setValueObjectList((List)data);
        request.put(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
        final Response response = this.dataService.saveAllPerformanceReviewComments(request);
        final Identifier id = (Identifier)response.get(WorkflowParams.COUNT.name());
        if (id != null) {
            return id.getId();
        }
        return 0;
    }
}

package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.domain.*;
import com.kpisoft.strategy.program.domain.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.*;
import com.canopus.mwworkflow.*;
import org.activiti.bpmn.model.*;
import org.activiti.engine.task.*;
import org.activiti.engine.task.Task;
import org.modelmapper.*;
import java.lang.reflect.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.user.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;
import com.kpisoft.user.vo.param.*;
import java.util.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ WorkflowManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class WorkflowManagerServiceImpl extends BaseMiddlewareBean implements WorkflowManagerService
{
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    private WorkflowManager workflowManager;
    @Autowired
    private ScorecardManager scorecardManager;
    @Autowired
    private WorkflowDomainManager workflowDomainManager;
    @Autowired
    private PerformanceProgramManager programManager;
    private static final Logger log;
    
    public WorkflowManagerServiceImpl() {
        this.serviceLocator = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response goalSettingTrigger(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(WorkflowParams.SCORECARD_ID.name());
        final BaseValueObjectMap workflowLeveles = (BaseValueObjectMap)request.get(WorkflowParams.WORKFLOW_LEVELS.name());
        if (scorecardId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final BpmnModel model = this.workflowDomainManager.defineWorkflowModel(workflowLeveles, scorecardId.getId(), WorkflowType.GOAL_SETTING, WorkflowParams.SCORECARDSUBMIT.name());
            this.workflowManager.createStartWorkflowProcess(model);
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - goalSettingTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_GOAL_SETTING_009.name(), "Failed to trigger the goalSetting", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response scorecardSubmitTrigger(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(WorkflowParams.SCORECARD_ID.name());
        final StringIdentifier userId = (StringIdentifier)request.get(WorkflowParams.USER_ID.name());
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final Identifier scorecardState = (Identifier)request.get(WorkflowParams.SCORECARD_STATE.name());
        final IdentifierList scorecardIdList = new IdentifierList((List)new ArrayList());
        final List<Integer> idList = new ArrayList<Integer>();
        if (scorecardId == null || userId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userId.getId(), scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
            this.workflowManager.completeTask(tasks.get(0).getId());
            if (objectList != null && objectList.getValueObjectList() != null && !objectList.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
                this.saveAllGoalSettingComments(request);
            }
            Integer state = ScorecardState.SUBMITTED.ordinal();
            if (scorecardState != null && scorecardState.getId() != null) {
                state = scorecardState.getId();
            }
            idList.add(scorecardId.getId());
            scorecardIdList.setIdsList((List)idList);
            this.scorecardManager.updateScorecardStatus(scorecardIdList, state);
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - scorecardSubmitTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_SUB_SCORECARD_010.name(), "Failed to trigger scorecard submition", (Throwable)ex));
        }
    }
    
    public Response getWorkflowTaskList(final Request request) {
        final StringIdentifier userId = (StringIdentifier)request.get(WorkflowParams.USER_ID.name());
        if (userId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_WFTL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskList(userId.getId());
            final Type listType = new TypeToken<List<WorkFlowTaskData>>() {}.getType();
            final ModelMapper modelMapper = new ModelMapper();
            final List<WorkFlowTaskData> valObjList = (List<WorkFlowTaskData>)modelMapper.map((Object)tasks, listType);
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)valObjList);
            return this.OK(WorkflowParams.WORK_FLOW_TASK_LIST.name(), (BaseValueObject)baseValueObjectList);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - getWorkflowTaskList() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_WFTL_UNABLE_TO_GET_004.name(), "Failed to load WorkflowTask list", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getWorkflowTaskListByEntityId(final Request request) {
        final StringIdentifier entityId = (StringIdentifier)request.get(WorkflowParams.ENTITY_ID.name());
        final StringIdentifier userId = (StringIdentifier)request.get(WorkflowParams.USER_ID.name());
        final StringIdentifier workflowType = (StringIdentifier)request.get(WorkflowParams.WORK_FLOW_TYPE.name());
        if (userId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_WFTL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userId.getId(), entityId.getId(), WorkflowType.valueOf(workflowType.getId()));
            final Type listType = new TypeToken<List<WorkFlowTaskData>>() {}.getType();
            final ModelMapper modelMapper = new ModelMapper();
            final List<WorkFlowTaskData> valObjList = (List<WorkFlowTaskData>)modelMapper.map((Object)tasks, listType);
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)valObjList);
            return this.OK(WorkflowParams.WORK_FLOW_TASK_LIST.name(), (BaseValueObject)baseValueObjectList);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - getWorkflowTaskListByEntityId() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_WFTL_UNABLE_TO_GET_BY_ENT_ID_009.name(), "Failed to load WorkflowTask list by entityId", (Throwable)ex));
        }
    }
    
    public Response goalSettingReviewOrApproveTrigger(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(WorkflowParams.SCORECARD_ID.name());
        final StringIdentifier userId = (StringIdentifier)request.get(WorkflowParams.USER_ID.name());
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final BooleanResponse isApproved = (BooleanResponse)request.get(WorkflowParams.IS_APPROVED.name());
        final Identifier scorecardState = (Identifier)request.get(WorkflowParams.SCORECARD_STATE.name());
        final IdentifierList scorecardIdList = new IdentifierList((List)new ArrayList());
        final List<Integer> idList = new ArrayList<Integer>();
        if (scorecardId == null || userId == null || isApproved == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userId.getId(), scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
            this.workflowManager.completeReviewTask(tasks.get(0).getId(), isApproved.isResponse());
            if (objectList != null && objectList.getValueObjectList() != null && !objectList.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
                this.saveAllGoalSettingComments(request);
            }
            final boolean isWorkflowCompleted = this.workflowManager.isWorkflowComplete(scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
            if (isWorkflowCompleted) {
                Integer state = ScorecardState.PUBLISHED.ordinal();
                if (scorecardState != null && scorecardState.getId() != null) {
                    state = scorecardState.getId();
                }
                idList.add(scorecardId.getId());
                scorecardIdList.setIdsList((List)idList);
                this.scorecardManager.updateScorecardStatus(scorecardIdList, state);
            }
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - goalSettingReviewOrApproveTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_REVIEW_OR_APPR_GS_012.name(), "Failed to trigger for Review Or Approve GoalSettign", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response scoreSubmitTrigger(final Request request) {
        final Identifier empId = (Identifier)request.get(WorkflowParams.EMP_ID.name());
        final Identifier orgId = (Identifier)request.get(WorkflowParams.ORG_ID.name());
        final Identifier scoreId = (Identifier)request.get(WorkflowParams.SCORE_ID.name());
        final BaseValueObjectList comments = (BaseValueObjectList)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name());
        final BaseValueObjectMap workflowLeveles = (BaseValueObjectMap)request.get(WorkflowParams.WORKFLOW_LEVELS.name());
        if (scoreId == null || scoreId.getId() == null || scoreId.getId() <= 0 || ((orgId == null || orgId.getId() == null) && (empId == null || empId.getId() == null))) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        String userOrOrgId = "";
        try {
            if (orgId != null && orgId.getId() != null && orgId.getId() > 0) {
                userOrOrgId = orgId.getId() + "";
            }
            else {
                userOrOrgId = this.getUserStringIdentifier(empId.getId());
            }
            final BpmnModel model = this.workflowDomainManager.defineWorkflowModel(workflowLeveles, scoreId.getId(), WorkflowType.REVIEW, WorkflowParams.SCORESUBMIT.name());
            this.workflowManager.createStartWorkflowProcess(model);
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userOrOrgId, scoreId.getId().toString(), WorkflowType.REVIEW);
            this.workflowManager.completeTask(tasks.get(0).getId());
            if (comments != null && comments.getValueObjectList() != null && !comments.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name(), (BaseValueObject)comments);
                this.saveAllPerformanceReviewComments(request);
            }
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - scoreSubmitTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_SUB_SCORE_011.name(), "Failed to trigger score submission", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response performanceReviewOrApproveTrigger(final Request request) {
        final Identifier scoreId = (Identifier)request.get(WorkflowParams.SCORE_ID.name());
        final StringIdentifier userId = (StringIdentifier)request.get(WorkflowParams.USER_ID.name());
        final BaseValueObjectList comments = (BaseValueObjectList)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name());
        final BooleanResponse isApproved = (BooleanResponse)request.get(WorkflowParams.IS_APPROVED.name());
        final Identifier orgId = (Identifier)request.get(WorkflowParams.ORG_ID.name());
        if (scoreId == null || isApproved == null || ((orgId == null || orgId.getId() == null) && (userId == null || userId.getId() == null))) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            String userOrOrgId;
            if (orgId != null && orgId.getId() != null && orgId.getId() > 0) {
                userOrOrgId = orgId.getId() + "";
            }
            else {
                userOrOrgId = userId.getId();
            }
            final List<Task> tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userOrOrgId, scoreId.getId().toString(), WorkflowType.REVIEW);
            this.workflowManager.completeReviewTask(tasks.get(0).getId(), isApproved.isResponse());
            if (comments != null && comments.getValueObjectList() != null && !comments.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name(), (BaseValueObject)comments);
                this.saveAllPerformanceReviewComments(request);
            }
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - performanceReviewOrApproveTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_REVIEW_OR_APPR_PERF_013.name(), "failed to trigger for PerformanceReview Or Approve", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response saveGoalSettingComments(final Request request) {
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)request.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.workflowDomainManager.saveGoalSettingComments(data);
            return this.OK(WorkflowParams.GOAL_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - saveGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_CREATE_003.name(), "Failed to save goalSettingWorkflowComments data", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateGoalSettingComments(final Request request) {
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)request.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.workflowDomainManager.saveGoalSettingComments(data);
            return this.OK(WorkflowParams.GOAL_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - updateGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_UPDATE_005.name(), "Failed to update GoalSettingWorkflowComments data", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getGoalSettingCommentsById(final Request request) {
        final Identifier identifier = (Identifier)request.get(WorkflowParams.GOAL_COMMENTS_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final GoalSettingWorkflowCommentsBean data = this.workflowDomainManager.getGoalSettingCommentsById(identifier.getId());
            return this.OK(WorkflowParams.GOAL_COMMENTS_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - getGoalSettingCommentsById() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_GET_004.name(), "Failed to load GoalSettingWorkflowComments", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchGoalSettingComments(final Request request) {
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)request.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<GoalSettingWorkflowCommentsBean> result = this.workflowDomainManager.searchGoalSettingComments(data);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - searchGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_SEARCH_008.name(), "Failed to search GoalSettingWorkflowComments", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response savePerformanceComments(final Request request) {
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.workflowDomainManager.savePerformanceComments(data);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - savePerformanceComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_CREATE_003.name(), "Exception in WorkflowManagerServiceImpl - savePerformanceComments() : ", (Throwable)ex));
        }
    }
    
    public Response updatePerformanceComments(final Request request) {
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.workflowDomainManager.savePerformanceComments(data);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - updatePerformanceComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_UPDATE_005.name(), "Exception in WorkflowManagerServiceImpl - updatePerformanceComments() : ", (Throwable)ex));
        }
    }
    
    public Response getPerformanceCommentsById(final Request request) {
        final Identifier identifier = (Identifier)request.get(WorkflowParams.PERFORMANCE_COMMENTS_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final PerformanceReviewCommentsBean data = this.workflowDomainManager.getPerformanceCommentsById(identifier.getId());
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - getPerformanceCommentsById() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_GET_004.name(), "Exception in WorkflowManagerServiceImpl - getPerformanceCommentsById() : ", (Throwable)ex));
        }
    }
    
    public Response searchPerformanceComments(final Request request) {
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<PerformanceReviewCommentsBean> result = this.workflowDomainManager.searchPerformanceComments(data);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - searchPerformanceComments() : " + ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_SEARCH_008.name(), "Exception in WorkflowManagerServiceImpl - searchPerformanceComments() : ", (Throwable)ex));
        }
    }
    
    private UserManagerService getUserService() {
        return (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
    }
    
    private String getUserStringIdentifier(final Integer employeeId) {
        UserData userData = new UserData();
        userData.setEmployeeId(employeeId);
        final SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setBaseValueObject((BaseValueObject)userData);
        final Request request = new Request();
        request.put(UMSParams.SEARCH_CRITERIA.name(), (BaseValueObject)searchCriteria);
        final Response response = this.getUserService().search(request);
        final BaseValueObjectList bValObjList = (BaseValueObjectList) response.getResponseValueObjects().get(UMSParams.USER_DATA_LIST.name());
        userData = (UserData) bValObjList.getValueObjectList().get(0);
        return userData.getId().toString();
    }
    
    public Response saveAllGoalSettingComments(final Request request) {
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final List<GoalSettingWorkflowCommentsBean> data = (List<GoalSettingWorkflowCommentsBean>)objectList.getValueObjectList();
        if (data == null || data.isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data objects in the request"));
        }
        try {
            final Integer count = this.workflowDomainManager.saveAllGoalSettingComments(data);
            return this.OK(WorkflowParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - saveAllGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_SAVE_ALL_009.name(), "Failed to save list of GoalSettingWorkflowComments data", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response saveAllPerformanceReviewComments(final Request request) {
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name());
        final List<PerformanceReviewCommentsBean> data = (List<PerformanceReviewCommentsBean>)objectList.getValueObjectList();
        if (data == null || data.isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data objects in the request"));
        }
        try {
            final Integer count = this.workflowDomainManager.saveAllPerformanceReviewComments(data);
            return this.OK(WorkflowParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - saveAllPerformanceReviewComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_SAVE_ALL_009.name(), "Failed to save list of PerformanceReviewComments data", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response scorecardSubmitTriggerForMultiple(final Request request) {
        final BaseValueObjectMap map = (BaseValueObjectMap)request.get(WorkflowParams.USER_SCORECARD_MAP.name());
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final Identifier scorecardState = (Identifier)request.get(WorkflowParams.SCORECARD_STATE.name());
        if (map == null || map.getBaseValueMap() == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            List<Task> tasks = new ArrayList<Task>();
            final IdentifierList scorecardIdList = new IdentifierList((List)new ArrayList());
            final List<Integer> idsList = new ArrayList<Integer>();
            for (final Map.Entry<? extends BaseValueObject, ? extends BaseValueObject> mapValue : map.getBaseValueMap().entrySet()) {
                final StringIdentifier userId = (StringIdentifier)mapValue.getKey();
                final Identifier scorecardId = (Identifier)mapValue.getValue();
                if (userId != null && userId.getId() != null && scorecardId != null && scorecardId.getId() > 0) {
                    tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userId.getId().toString(), scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
                    if (tasks.isEmpty()) {
                        continue;
                    }
                    this.workflowManager.completeTask(tasks.get(0).getId());
                    idsList.add(scorecardId.getId());
                }
            }
            if (objectList != null && objectList.getValueObjectList() != null && !objectList.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
                this.saveAllGoalSettingComments(request);
            }
            Integer state = ScorecardState.SUBMITTED.ordinal();
            if (scorecardState != null && scorecardState.getId() != null) {
                state = scorecardState.getId();
            }
            scorecardIdList.setIdsList((List)idsList);
            this.scorecardManager.updateScorecardStatus(scorecardIdList, state);
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - scorecardSubmitTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_SUB_SCORECARD_010.name(), "Failed to trigger scorecard submition", (Throwable)ex));
        }
    }
    
    public Response goalSettingReviewOrApproveTriggerMultiple(final Request request) {
        final BaseValueObjectMap map = (BaseValueObjectMap)request.get(WorkflowParams.USER_SCORECARD_MAP.name());
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final BooleanResponse isApproved = (BooleanResponse)request.get(WorkflowParams.IS_APPROVED.name());
        final Identifier scorecardState = (Identifier)request.get(WorkflowParams.SCORECARD_STATE.name());
        if (map == null || map.getBaseValueMap() == null || isApproved == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            List<Task> tasks = new ArrayList<Task>();
            final IdentifierList scorecardIdList = new IdentifierList((List)new ArrayList());
            final List<Integer> idsList = new ArrayList<Integer>();
            for (final Map.Entry<? extends BaseValueObject, ? extends BaseValueObject> mapValue : map.getBaseValueMap().entrySet()) {
                final StringIdentifier userId = (StringIdentifier)mapValue.getKey();
                final Identifier scorecardId = (Identifier)mapValue.getValue();
                if (userId != null && userId.getId() != null && scorecardId != null && scorecardId.getId() > 0) {
                    tasks = (List<Task>)this.workflowManager.getWorkflowTaskListForProcess(userId.getId().toString(), scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
                    if (tasks.isEmpty()) {
                        continue;
                    }
                    this.workflowManager.completeReviewTask(tasks.get(0).getId(), isApproved.isResponse());
                    final boolean isWorkflowCompleted = this.workflowManager.isWorkflowComplete(scorecardId.getId().toString(), WorkflowType.GOAL_SETTING);
                    if (!isWorkflowCompleted) {
                        continue;
                    }
                    idsList.add(scorecardId.getId());
                }
            }
            scorecardIdList.setIdsList((List)idsList);
            if (objectList != null && objectList.getValueObjectList() != null && !objectList.getValueObjectList().isEmpty()) {
                final Request req = new Request();
                req.put(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)objectList);
                this.saveAllGoalSettingComments(request);
            }
            Integer state = ScorecardState.PUBLISHED.ordinal();
            if (scorecardState != null && scorecardState.getId() != null) {
                state = scorecardState.getId();
            }
            this.scorecardManager.updateScorecardStatus(scorecardIdList, state);
            return this.OK(WorkflowParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            WorkflowManagerServiceImpl.log.error("Exception in WorkflowManagerServiceImpl - goalSettingReviewOrApproveTrigger() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_TRIGGER_UNABLE_TO_REVIEW_OR_APPR_GS_012.name(), "Failed to trigger for Review Or Approve GoalSettign", new Object[] { ex.getMessage() }));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)WorkflowManagerServiceImpl.class);
    }
}

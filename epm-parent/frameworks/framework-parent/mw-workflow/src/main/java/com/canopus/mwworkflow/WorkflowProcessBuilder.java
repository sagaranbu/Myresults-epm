package com.canopus.mwworkflow;

import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.util.*;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;

public class WorkflowProcessBuilder
{
    public static char DELIM;
    private static String START_TASK_ID;
    private static String END_TASK_ID;
    private static String CANCEL_TASK_ID;
    private String entityId;
    private WorkflowType workflowType;
    private BpmnModel model;
    private Process process;
    private String sourceTaskId;
    private String firstTaskId;
    private Integer tenantId;
    
    public String getTaskId(final String levelName, final String userId) {
        return levelName + WorkflowProcessBuilder.DELIM + userId + WorkflowProcessBuilder.DELIM + WorkflowManager.getProcessId(this.workflowType, this.entityId, this.tenantId);
    }
    
    public String getTaskLevelId(final String levelName) {
        return levelName + WorkflowProcessBuilder.DELIM + WorkflowManager.getProcessId(this.workflowType, this.entityId, this.tenantId);
    }
    
    public static WorkflowProcessBuilder createWorkflowProcessBuilder() {
        return new WorkflowProcessBuilder();
    }
    
    private WorkflowProcessBuilder() {
        this.model = new BpmnModel();
        this.process = new Process();
        this.sourceTaskId = WorkflowProcessBuilder.START_TASK_ID;
        this.firstTaskId = null;
        this.tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        this.model.addProcess(this.process);
    }
    
    public WorkflowProcessBuilder setEntityId(final String entityId) {
        this.entityId = entityId;
        return this;
    }
    
    public WorkflowProcessBuilder setWorkflowType(final WorkflowType workflowType) {
        this.workflowType = workflowType;
        return this;
    }
    
    public WorkflowProcessBuilder addReviewersAtLevel(final String levelName, final List<String> managerUserIds) {
        for (final String managerUserId : managerUserIds) {
            final String taskId = this.getTaskId(levelName, managerUserId) + WorkflowProcessBuilder.DELIM + "pl";
            this.process.addFlowElement((FlowElement)this.createUserTask(taskId, taskId, managerUserId));
        }
        final String taskLevelId = this.getTaskLevelId(levelName);
        final String forkId = taskLevelId + "_fork";
        this.process.addFlowElement(this.createParallelGateway(forkId, forkId));
        final String joinId = taskLevelId + "_join";
        this.process.addFlowElement(this.createParallelGateway(joinId, joinId));
        this.process.addFlowElement((FlowElement)this.createUserApproveRejectTask(taskLevelId, taskLevelId, null));
        final String exGateId = taskLevelId + "_ex";
        this.process.addFlowElement(createExclusiveGateway(exGateId, exGateId));
        this.process.addFlowElement((FlowElement)this.createSequenceFlow(joinId, taskLevelId));
        this.process.addFlowElement((FlowElement)this.createSequenceFlow(taskLevelId, exGateId));
        this.connectSourceToCurrent(forkId);
        for (final String managerUserId2 : managerUserIds) {
            final String taskId2 = this.getTaskId(levelName, managerUserId2) + WorkflowProcessBuilder.DELIM + "pl";
            this.process.addFlowElement((FlowElement)this.createSequenceFlow(forkId, taskId2));
            this.process.addFlowElement((FlowElement)this.createSequenceFlow(taskId2, joinId));
        }
        this.process.addFlowElement(createCondSequenceFlow(exGateId, this.firstTaskId, "${isApproved == 'reject'}"));
        this.sourceTaskId = exGateId;
        return this;
    }
    
    public WorkflowProcessBuilder addReviewerAtLevel(final String levelName, final String managerUserId) {
        final String taskId = this.getTaskId(levelName, managerUserId);
        this.process.addFlowElement((FlowElement)this.createUserApproveRejectTask(taskId, taskId, managerUserId));
        final String exGateId = taskId + "_ex";
        this.process.addFlowElement(createExclusiveGateway(exGateId, exGateId));
        this.process.addFlowElement((FlowElement)this.createSequenceFlow(taskId, exGateId));
        this.process.addFlowElement(createCondSequenceFlow(exGateId, this.firstTaskId, "${isApproved == 'reject'}"));
        this.connectSourceToCurrent(taskId);
        this.sourceTaskId = exGateId;
        return this;
    }
    
    private void connectSourceToCurrent(final String taskId) {
        if (this.sourceTaskId.endsWith("_ex")) {
            this.process.addFlowElement(createCondSequenceFlow(this.sourceTaskId, taskId, "${isApproved == 'approve'}"));
        }
        else {
            this.process.addFlowElement((FlowElement)this.createSequenceFlow(this.sourceTaskId, taskId));
        }
    }
    
    public WorkflowProcessBuilder addActivityInWorkflow(final String levelName, final String userId) {
        final String taskId = this.getTaskId(levelName, userId);
        this.process.addFlowElement((FlowElement)this.createUserTask(taskId, taskId, userId));
        this.connectSourceToCurrent(taskId);
        if (this.firstTaskId == null) {
            this.firstTaskId = taskId;
        }
        this.sourceTaskId = taskId;
        return this;
    }
    
    public BpmnModel build() {
        if (this.entityId == null || this.workflowType == null) {
            throw new RuntimeException("EntityID and WorkflowType are not set");
        }
        if (this.process.getFlowElements().size() == 0) {
            throw new RuntimeException("Cannot create empty process.");
        }
        this.process.addFlowElement((FlowElement)this.createStartEvent());
        this.process.addFlowElement((FlowElement)this.createEndEvent());
        this.connectSourceToCurrent(WorkflowProcessBuilder.END_TASK_ID);
        final String processId = WorkflowManager.getProcessId(this.workflowType, this.entityId, this.tenantId);
        this.process.setId(processId);
        this.process.setName(processId);
        return this.model;
    }
    
    protected static FlowElement createCondSequenceFlow(final String from, final String to, final String expr) {
        final SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setConditionExpression(expr);
        return (FlowElement)flow;
    }
    
    protected static FlowElement createExclusiveGateway(final String id, final String name) {
        final ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(id);
        gateway.setName(name);
        return (FlowElement)gateway;
    }
    
    protected FlowElement createParallelGateway(final String id, final String name) {
        final ParallelGateway gateway = new ParallelGateway();
        gateway.setId(id);
        gateway.setName(name);
        return (FlowElement)gateway;
    }
    
    protected UserTask createUserTask(final String id, final String name, final String assignee) {
        final UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee + WorkflowProcessBuilder.DELIM + this.tenantId);
        return userTask;
    }
    
    protected UserTask createUserApproveRejectTask(final String id, final String name, final String assignee) {
        final UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        if (assignee != null) {
            userTask.setAssignee(assignee + WorkflowProcessBuilder.DELIM + this.tenantId);
        }
        final List<FormProperty> formProperties = new ArrayList<FormProperty>();
        final FormProperty prop = new FormProperty();
        prop.setId("isApproved");
        prop.setName("isApproved");
        final List<FormValue> values = new ArrayList<FormValue>();
        FormValue value = new FormValue();
        value.setId("approve");
        value.setName("approve");
        values.add(value);
        value = new FormValue();
        value.setId("reject");
        value.setName("reject");
        values.add(value);
        prop.setFormValues((List)values);
        prop.setRequired(true);
        prop.setType("enum");
        formProperties.add(prop);
        userTask.setFormProperties((List)formProperties);
        return userTask;
    }
    
    protected SequenceFlow createSequenceFlow(final String from, final String to) {
        final SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }
    
    protected StartEvent createStartEvent() {
        final StartEvent startEvent = new StartEvent();
        startEvent.setId(WorkflowProcessBuilder.START_TASK_ID);
        return startEvent;
    }
    
    protected EndEvent createEndEvent() {
        final EndEvent endEvent = new EndEvent();
        endEvent.setId(WorkflowProcessBuilder.END_TASK_ID);
        return endEvent;
    }
    
    static {
        WorkflowProcessBuilder.DELIM = '.';
        WorkflowProcessBuilder.START_TASK_ID = "start";
        WorkflowProcessBuilder.END_TASK_ID = "end";
        WorkflowProcessBuilder.CANCEL_TASK_ID = "cancel";
    }
}

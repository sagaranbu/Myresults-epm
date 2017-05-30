package com.canopus.mwworkflow;

import org.springframework.stereotype.*;
import org.activiti.engine.task.*;
import org.activiti.engine.task.Task;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.*;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.identity.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.util.*;

@Component
public class WorkflowManager
{
    public static String getProcessId(final WorkflowType workflowType, final String entityId, final Integer tenantId) {
        return workflowType.name() + WorkflowProcessBuilder.DELIM + entityId + WorkflowProcessBuilder.DELIM + tenantId;
    }
    
    public static WorkflowTaskParams getTaskBusinessParams(final Task task) {
        final WorkflowTaskParams params = new WorkflowTaskParams();
        final String[] split0 = task.getName().split("\\" + WorkflowProcessBuilder.DELIM);
        params.workflowStage = split0[0];
        params.assignee = split0[1];
        params.tenantId = Integer.parseInt(split0[4]);
        params.workflowType = WorkflowType.valueOf(split0[2]);
        params.entityId = split0[3];
        params.taskId = task.getId();
        return params;
    }
    
    public void createStartWorkflowProcess(final BpmnModel model) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Deployment deployment = processEngine.getRepositoryService().createDeployment().addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment").deploy();
        final ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(model.getProcesses().get(0).getName());
    }
    
    public void createWorkflowUser(final String userId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final User user = (User)new UserEntity();
        user.setId(userId);
        processEngine.getIdentityService().saveUser(user);
    }
    
    public void deleteWorkflowUser(final String userId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getIdentityService().deleteUser(userId);
    }
    
    public List<Task> getWorkflowTaskList(final String userId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        return (List<Task>)processEngine.getTaskService().createTaskQuery().taskAssignee(userId + WorkflowProcessBuilder.DELIM + tenantId).list();
    }
    
    public List<Task> getWorkflowTaskListForProcess(final String entityId, final WorkflowType workflowType) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        final String processKey = getProcessId(workflowType, entityId, tenantId);
        return (List<Task>)processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).list();
    }
    
    public List<Task> getWorkflowTaskListForProcess(final String userId, final String entityId, final WorkflowType workflowType) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        final String processKey = getProcessId(workflowType, entityId, tenantId);
        final List<Task> tasks = (List<Task>)processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).list();
        final List<Task> resultTasks = new ArrayList<Task>();
        for (final Task task : tasks) {
            final WorkflowTaskParams taskParams = getTaskBusinessParams(task);
            if (taskParams.getAssignee().equalsIgnoreCase(userId)) {
                resultTasks.add(task);
            }
        }
        return resultTasks;
    }
    
    public List<Task> getWorkflowTaskListForProcess(final String entityId, final WorkflowType workflowType, final Integer tenantId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final String processKey = getProcessId(workflowType, entityId, tenantId);
        return (List<Task>)processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).list();
    }
    
    public List<Task> getWorkflowTaskList(final String userId, final WorkflowType workflowType) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        final List<Task> tasks = (List<Task>)processEngine.getTaskService().createTaskQuery().taskAssignee(userId + WorkflowProcessBuilder.DELIM + tenantId).list();
        final List<Task> resultTasks = new ArrayList<Task>();
        for (final Task task : tasks) {
            final WorkflowTaskParams taskParams = getTaskBusinessParams(task);
            if (taskParams.getWorkflowType() == workflowType) {
                resultTasks.add(task);
            }
        }
        return resultTasks;
    }
    
    public List<Task> getWorkflowTaskList(final String userId, final Integer tenantId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return (List<Task>)processEngine.getTaskService().createTaskQuery().taskAssignee(userId + WorkflowProcessBuilder.DELIM + tenantId).list();
    }
    
    public List<Task> getWorkflowTaskList(final String userId, final WorkflowType workflowType, final Integer tenantId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final List<Task> tasks = (List<Task>)processEngine.getTaskService().createTaskQuery().taskAssignee(userId + WorkflowProcessBuilder.DELIM + tenantId).list();
        final List<Task> resultTasks = new ArrayList<Task>();
        for (final Task task : tasks) {
            final WorkflowTaskParams taskParams = getTaskBusinessParams(task);
            if (taskParams.getWorkflowType() == workflowType) {
                resultTasks.add(task);
            }
        }
        return resultTasks;
    }
    
    public void completeTask(final String taskId) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskId);
    }
    
    public void completeReviewTask(final String taskId, final boolean isApproved) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Map<String, Object> variables = null;
        final List<Task> tasks = (List<Task>)processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
        final Task currentTask = tasks.get(0);
        if (currentTask.getName().endsWith(WorkflowProcessBuilder.DELIM + "pl")) {
            List<Task> tasksInWorkFlow = (List<Task>)processEngine.getTaskService().createTaskQuery().processInstanceId(currentTask.getProcessInstanceId()).list();
            if (!isApproved) {
                for (final Task task : tasksInWorkFlow) {
                    processEngine.getTaskService().complete(task.getId());
                }
                tasksInWorkFlow = (List<Task>)processEngine.getTaskService().createTaskQuery().processInstanceId(currentTask.getProcessInstanceId()).list();
                final Task decisionTask = tasksInWorkFlow.get(0);
                processEngine.getTaskService().setAssignee(decisionTask.getId(), currentTask.getAssignee());
                variables = new HashMap<String, Object>();
                variables.put("isApproved", "reject");
                processEngine.getTaskService().complete(decisionTask.getId(), (Map)variables);
            }
            else {
                processEngine.getTaskService().complete(taskId);
                if (tasksInWorkFlow.size() == 1) {
                    tasksInWorkFlow = (List<Task>)processEngine.getTaskService().createTaskQuery().processInstanceId(currentTask.getProcessInstanceId()).list();
                    final Task decisionTask = tasksInWorkFlow.get(0);
                    processEngine.getTaskService().setAssignee(decisionTask.getId(), currentTask.getAssignee());
                    variables = new HashMap<String, Object>();
                    variables.put("isApproved", "approve");
                    processEngine.getTaskService().complete(decisionTask.getId(), (Map)variables);
                }
            }
        }
        else {
            variables = new HashMap<String, Object>();
            variables.put("isApproved", isApproved ? "approve" : "reject");
            processEngine.getTaskService().complete(taskId, (Map)variables);
        }
    }
    
    public boolean isWorkflowComplete(final String entityId, final WorkflowType workflowType) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.name());
        final String processKey = getProcessId(workflowType, entityId, tenantId);
        final long count = processEngine.getRuntimeService().createProcessInstanceQuery().active().processDefinitionKey(processKey).count();
        return count == 0L;
    }
    
    public static class WorkflowTaskParams
    {
        private WorkflowType workflowType;
        private String entityId;
        private String workflowStage;
        private String assignee;
        private String taskId;
        private Integer tenantId;
        
        public WorkflowType getWorkflowType() {
            return this.workflowType;
        }
        
        public void setWorkflowType(final WorkflowType workflowType) {
            this.workflowType = workflowType;
        }
        
        public String getEntityId() {
            return this.entityId;
        }
        
        public void setEntityId(final String entityId) {
            this.entityId = entityId;
        }
        
        public String getWorkflowStage() {
            return this.workflowStage;
        }
        
        public void setWorkflowStage(final String workflowStage) {
            this.workflowStage = workflowStage;
        }
        
        public String getAssignee() {
            return this.assignee;
        }
        
        public void setAssignee(final String assignee) {
            this.assignee = assignee;
        }
        
        public String getTaskId() {
            return this.taskId;
        }
        
        public void setTaskId(final String taskId) {
            this.taskId = taskId;
        }
        
        public Integer getTenantId() {
            return this.tenantId;
        }
        
        public void setTenantId(final Integer tenantId) {
            this.tenantId = tenantId;
        }
        
        @Override
        public String toString() {
            return "WorkflowTaskParams [" + ((this.workflowType != null) ? ("workflowType=" + this.workflowType + ", ") : "") + ((this.entityId != null) ? ("entityId=" + this.entityId + ", ") : "") + ((this.workflowStage != null) ? ("workflowStage=" + this.workflowStage + ", ") : "") + ((this.assignee != null) ? ("assignee=" + this.assignee + ", ") : "") + ((this.taskId != null) ? ("taskId=" + this.taskId + ", ") : "") + ((this.tenantId != null) ? ("tenantId=" + this.tenantId) : "") + "]";
        }
    }
}

package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.activiti.bpmn.model.*;
import com.canopus.mwworkflow.*;
import com.canopus.mw.dto.*;
import java.util.*;

public class WorkflowDomain extends BaseDomainObject
{
    public BpmnModel defineWorkflowModel(final BaseValueObjectMap workflowLeveles, final Integer entityId, final WorkflowType workflowType, final String activityWorkflowType) {
        final WorkflowProcessBuilder builder = WorkflowProcessBuilder.createWorkflowProcessBuilder().setEntityId(entityId + "");
        builder.setWorkflowType(workflowType);
        if (workflowLeveles.getBaseValueMap() != null && workflowLeveles.getBaseValueMap().size() > 0) {
            final Map<StringIdentifier, IdentifierList> levels = (Map<StringIdentifier, IdentifierList>)workflowLeveles.getBaseValueMap();
            boolean isEntered = true;
            for (final Map.Entry<StringIdentifier, IdentifierList> entry : levels.entrySet()) {
                if (entry.getKey() != null && entry.getKey().getId() != null) {
                    if (isEntered) {
                        isEntered = false;
                        builder.addActivityInWorkflow(activityWorkflowType, entry.getValue().getIdsList().get(0).toString());
                    }
                    else {
                        final List<String> multiple = new ArrayList<String>();
                        for (final Integer id : entry.getValue().getIdsList()) {
                            multiple.add(id + "");
                        }
                        builder.addReviewersAtLevel(entry.getKey().getId(), (List)multiple);
                    }
                }
            }
        }
        return builder.build();
    }
}

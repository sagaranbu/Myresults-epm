package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import org.activiti.engine.task.*;
import java.util.*;

public class WorkFlowTaskData extends BaseValueObject
{
    private static final long serialVersionUID = -4126055130260012701L;
    private String id;
    private String name;
    private DelegationState delegationState;
    private String description;
    private Date createTime;
    private Date dueDate;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public DelegationState getDelegationState() {
        return this.delegationState;
    }
    
    public void setDelegationState(final DelegationState delegationState) {
        this.delegationState = delegationState;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }
}

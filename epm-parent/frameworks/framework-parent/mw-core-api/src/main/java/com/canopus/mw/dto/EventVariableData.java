package com.canopus.mw.dto;

import java.util.*;

public class EventVariableData extends BaseValueObject
{
    private Integer id;
    private Integer eventId;
    private String variableName;
    private String variableDescription;
    private String resolverSourceType;
    private String resolverServiceOperation;
    private String resolverServiceInput;
    private String resolver;
    private List<EventVariableCategoryData> variableCategories;
    
    public EventVariableData() {
        this.variableCategories = new ArrayList<EventVariableCategoryData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEventId() {
        return this.eventId;
    }
    
    public void setEventId(final Integer eventId) {
        this.eventId = eventId;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
    
    public String getVariableDescription() {
        return this.variableDescription;
    }
    
    public void setVariableDescription(final String variableDescription) {
        this.variableDescription = variableDescription;
    }
    
    public String getResolverSourceType() {
        return this.resolverSourceType;
    }
    
    public void setResolverSourceType(final String resolverSourceType) {
        this.resolverSourceType = resolverSourceType;
    }
    
    public String getResolverServiceOperation() {
        return this.resolverServiceOperation;
    }
    
    public void setResolverServiceOperation(final String resolverServiceOperation) {
        this.resolverServiceOperation = resolverServiceOperation;
    }
    
    public String getResolverServiceInput() {
        return this.resolverServiceInput;
    }
    
    public void setResolverServiceInput(final String resolverServiceInput) {
        this.resolverServiceInput = resolverServiceInput;
    }
    
    public String getResolver() {
        return this.resolver;
    }
    
    public void setResolver(final String resolver) {
        this.resolver = resolver;
    }
    
    public List<EventVariableCategoryData> getVariableCategories() {
        return this.variableCategories;
    }
    
    public void setVariableCategories(final List<EventVariableCategoryData> variableCategories) {
        this.variableCategories = variableCategories;
    }
}

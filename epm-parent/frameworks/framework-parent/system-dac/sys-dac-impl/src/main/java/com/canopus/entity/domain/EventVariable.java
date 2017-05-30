package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_MET_EVENT_VARIABLE")
public class EventVariable extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -5165487055350626031L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EVENT_VARIABLE_ID_SEQ")
    @SequenceGenerator(name = "EVENT_VARIABLE_ID_SEQ", sequenceName = "EVENT_VARIABLE_ID_SEQ")
    @Column(name = "EVENT_VARIABLE_PK_ID", length = 11)
    private Integer id;
    @Column(name = "EVENT_FK_ID")
    private Integer eventId;
    @Column(name = "VARIABLE_NAME")
    private String variableName;
    @Column(name = "VARIABLE_DESCRIPTION")
    private String variableDescription;
    @Column(name = "RESOLVER_SOURCE_TYPE")
    private String resolverSourceType;
    @Column(name = "RESOLVER_SERVICE_OPERATION")
    private String resolverServiceOperation;
    @Column(name = "RESOLVER_SERVICE_INPUT")
    private String resolverServiceInput;
    @Column(name = "RESOLVER")
    private String resolver;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "VARIABLE_FK_ID")
    private List<EventVariableCategory> variableCategories;
    
    public EventVariable() {
        this.variableCategories = new ArrayList<EventVariableCategory>();
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
    
    public List<EventVariableCategory> getVariableCategories() {
        return this.variableCategories;
    }
    
    public void setVariableCategories(final List<EventVariableCategory> variableCategories) {
        this.variableCategories = variableCategories;
    }
}

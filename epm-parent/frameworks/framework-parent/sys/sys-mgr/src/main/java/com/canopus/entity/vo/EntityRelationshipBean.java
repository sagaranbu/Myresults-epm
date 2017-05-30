package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class EntityRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = -7492883117770445424L;
    private Integer id;
    private Integer sourceEntityId;
    private Integer destinationEntityId;
    private String relationship;
    private String name;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getSourceEntityId() {
        return this.sourceEntityId;
    }
    
    public void setSourceEntityId(final Integer sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }
    
    public Integer getDestinationEntityId() {
        return this.destinationEntityId;
    }
    
    public void setDestinationEntityId(final Integer destinationEntityId) {
        this.destinationEntityId = destinationEntityId;
    }
    
    public String getRelationship() {
        return this.relationship;
    }
    
    public void setRelationship(final String relationship) {
        this.relationship = relationship;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}

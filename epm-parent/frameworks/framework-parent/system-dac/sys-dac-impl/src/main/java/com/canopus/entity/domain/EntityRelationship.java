package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_ENTITY_REL_BASE")
public class EntityRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -384062010227102119L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "REL_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "REL_ENTITY_ID_SEQ", sequenceName = "REL_ENTITY_ID_SEQ")
    @Column(name = "SMR_PK_ID")
    private Integer id;
    @Column(name = "SOURCE_ENTITY_ID", length = 127)
    private Integer sourceEntityId;
    @Column(name = "DESTINATION_ENTITY_ID", length = 127)
    private Integer destinationEntityId;
    @Column(name = "RELATIONSHIP", length = 127)
    private String relationship;
    @Column(name = "NAME", length = 127)
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

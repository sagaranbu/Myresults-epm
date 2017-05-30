package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_ENTITY_INSTANCE")
public class CustomEntityInstance extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 7589166691231363628L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ENTITY_INSTANCE_ID_SEQ")
    @SequenceGenerator(name = "COR_ENTITY_INSTANCE_ID_SEQ", sequenceName = "COR_ENTITY_INSTANCE_ID_SEQ")
    @Column(name = "CCI_PK_ID")
    private Integer id;
    @Column(name = "CCE_FK_ID", length = 11)
    private Integer entityId;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "entityInstance")
    private List<CustomEntityInstanceFieldValue> fields;
    @Column(name = "NAME", length = 512)
    private String name;
    @Column(name = "EVENT_HANDLER")
    private String eventHandler;
    
    public CustomEntityInstance() {
        this.fields = new ArrayList<CustomEntityInstanceFieldValue>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<CustomEntityInstanceFieldValue> getFields() {
        return this.fields;
    }
    
    public void setFields(final List<CustomEntityInstanceFieldValue> fields) {
        this.fields = fields;
    }
    
    public String getEventHandler() {
        return this.eventHandler;
    }
    
    public void setEventHandler(final String eventHandler) {
        this.eventHandler = eventHandler;
    }
}

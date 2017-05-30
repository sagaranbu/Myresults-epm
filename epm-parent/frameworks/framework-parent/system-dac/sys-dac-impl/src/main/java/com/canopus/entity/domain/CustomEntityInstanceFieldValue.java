package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_ENTITY_INSTANCE_DATA")
public class CustomEntityInstanceFieldValue extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 4158072428465672640L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_INSTANCE_FIELD_ID_SEQ")
    @SequenceGenerator(name = "COR_INSTANCE_FIELD_ID_SEQ", sequenceName = "COR_INSTANCE_FIELD_ID_SEQ")
    @Column(name = "CCD_PK_ID")
    private Integer id;
    @Column(name = "CCF_FK_ID", length = 11)
    private Integer entityFieldId;
    @Column(name = "VALUE")
    @Lob
    private String value;
    @ManyToOne
    @JoinColumn(name = "CCI_FK_ID", referencedColumnName = "CCI_PK_ID")
    private CustomEntityInstance entityInstance;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEntityFieldId() {
        return this.entityFieldId;
    }
    
    public void setEntityFieldId(final Integer entityFieldId) {
        this.entityFieldId = entityFieldId;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public CustomEntityInstance getEntityInstance() {
        return this.entityInstance;
    }
    
    public void setEntityInstance(final CustomEntityInstance entityInstance) {
        this.entityInstance = entityInstance;
    }
}

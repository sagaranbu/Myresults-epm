package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_RUL_FIELD_VALIDATION")
@SQLDelete(sql = "UPDATE COR_RUL_FIELD_VALIDATION SET IS_DELETED = 1 WHERE SRF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class FieldValidationRule extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 1996365871904937828L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "FIELD_RULE_ID_SEQ")
    @SequenceGenerator(name = "FIELD_RULE_ID_SEQ", sequenceName = "FIELD_RULE_ID_SEQ")
    @Column(name = "SRF_PK_ID")
    private Integer id;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "RULE", length = 512)
    private String rule;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMF_FK_ID", referencedColumnName = "CMF_PK_ID")
    private EntityField entityField;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public String getRule() {
        return this.rule;
    }
    
    public void setRule(final String rule) {
        this.rule = rule;
    }
    
    public EntityField getEntityField() {
        return this.entityField;
    }
    
    public void setEntityField(final EntityField entityField) {
        this.entityField = entityField;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

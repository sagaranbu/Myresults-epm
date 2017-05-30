package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_EVENT_VARIABLE_CAT")
public class EventVariableCategory extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 951578568552204008L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EVENT_VARIABLE_CAT_ID_SEQ")
    @SequenceGenerator(name = "EVENT_VARIABLE_CAT_ID_SEQ", sequenceName = "EVENT_VARIABLE_CAT_ID_SEQ")
    @Column(name = "EVENT_VARIABLE_CAT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "VARIABLE_FK_ID")
    private Integer variableId;
    @Column(name = "VARIABLE_CATEGORY", length = 50)
    private String variableCategory;
    @Column(name = "CATEGORY_DESCRIPTION", length = 1000)
    private String categoryDescription;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getVariableId() {
        return this.variableId;
    }
    
    public void setVariableId(final Integer variableId) {
        this.variableId = variableId;
    }
    
    public String getVariableCategory() {
        return this.variableCategory;
    }
    
    public void setVariableCategory(final String variableCategory) {
        this.variableCategory = variableCategory;
    }
    
    public String getCategoryDescription() {
        return this.categoryDescription;
    }
    
    public void setCategoryDescription(final String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}

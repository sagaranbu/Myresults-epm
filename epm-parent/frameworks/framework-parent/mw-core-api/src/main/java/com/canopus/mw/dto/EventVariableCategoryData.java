package com.canopus.mw.dto;

public class EventVariableCategoryData extends BaseValueObject
{
    private Integer id;
    private Integer variableId;
    private String variableCategory;
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

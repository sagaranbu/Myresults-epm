package com.canopus.mw.dto;

public class SearchCriteria extends BaseValueObject
{
    private BaseValueObject baseValueObject;
    private Integer maxResults;
    private Integer firstResult;
    
    public BaseValueObject getBaseValueObject() {
        return this.baseValueObject;
    }
    
    public void setBaseValueObject(final BaseValueObject baseValueObject) {
        this.baseValueObject = baseValueObject;
    }
    
    public Integer getMaxResults() {
        return this.maxResults;
    }
    
    public void setMaxResults(final Integer maxResults) {
        this.maxResults = maxResults;
    }
    
    public Integer getFirstResult() {
        return this.firstResult;
    }
    
    public void setFirstResult(final Integer firstResult) {
        this.firstResult = firstResult;
    }
}

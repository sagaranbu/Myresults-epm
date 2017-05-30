package com.canopus.mw.dto;

public class Page extends BaseValueObject
{
    private static final long serialVersionUID = 384990491572242999L;
    private Integer maxResults;
    private Integer pageNumber;
    private Integer totalCount;
    
    public Integer getMaxResults() {
        return this.maxResults;
    }
    
    public void setMaxResults(final Integer maxResults) {
        this.maxResults = maxResults;
    }
    
    public Integer getPageNumber() {
        return this.pageNumber;
    }
    
    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public Integer getTotalCount() {
        return this.totalCount;
    }
    
    public void setTotalCount(final Integer totalCount) {
        this.totalCount = totalCount;
    }
}

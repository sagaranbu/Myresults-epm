package com.canopus.mw.dto;

public class TagSummaryData extends BaseValueObject
{
    private Integer id;
    private TagData tag;
    private String context;
    private Integer count;
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public TagData getTag() {
        return this.tag;
    }
    
    public void setTag(final TagData tag) {
        this.tag = tag;
    }
    
    public String getContext() {
        return this.context;
    }
    
    public void setContext(final String context) {
        this.context = context;
    }
    
    public Integer getCount() {
        return this.count;
    }
    
    public void setCount(final Integer count) {
        this.count = count;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

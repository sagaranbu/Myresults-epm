package com.canopus.mw.dto;

public class StringIdentifier extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private String id;
    
    public StringIdentifier() {
    }
    
    public StringIdentifier(final String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "StringIdentifier [" + ((this.id != null) ? ("id=" + this.id) : "") + "]";
    }
}

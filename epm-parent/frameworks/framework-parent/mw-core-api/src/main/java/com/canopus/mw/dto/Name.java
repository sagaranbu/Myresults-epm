package com.canopus.mw.dto;

public class Name extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private String name;
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Name [" + ((this.name != null) ? ("name=" + this.name) : "") + "]";
    }
}

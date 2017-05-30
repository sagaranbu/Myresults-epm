package com.canopus.mw.dto;

public class DoubleIdentifier extends BaseValueObject
{
    private static final long serialVersionUID = -2584851604384216276L;
    private Double id;
    
    public DoubleIdentifier() {
    }
    
    public DoubleIdentifier(final Double id) {
        this.id = id;
    }
    
    public Double getId() {
        return this.id;
    }
    
    public void setId(final Double id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "DoubleIdentifier [" + ((this.id != null) ? ("id=" + this.id) : "") + "]";
    }
}

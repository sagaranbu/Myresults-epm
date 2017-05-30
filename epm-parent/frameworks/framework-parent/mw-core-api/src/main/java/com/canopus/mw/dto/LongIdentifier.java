package com.canopus.mw.dto;

public class LongIdentifier extends BaseValueObject
{
    private static final long serialVersionUID = -8593622112830683007L;
    private Long id;
    
    public LongIdentifier(final Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
}

package com.canopus.mw.dto;

public class Identifier extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private Integer id;
    
    public Identifier() {
    }
    
    public Identifier(final Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "Identifier [" + ((this.id != null) ? ("id=" + this.id) : "") + "]";
    }
}

package com.canopus.mw.dto;

import javax.validation.constraints.*;

public class IDMapData extends BaseValueObject
{
    @NotNull
    private Integer parent;
    @NotNull
    private Integer child;
    
    public Integer getParent() {
        return this.parent;
    }
    
    public void setParent(final Integer parent) {
        this.parent = parent;
    }
    
    public Integer getChild() {
        return this.child;
    }
    
    public void setChild(final Integer child) {
        this.child = child;
    }
}

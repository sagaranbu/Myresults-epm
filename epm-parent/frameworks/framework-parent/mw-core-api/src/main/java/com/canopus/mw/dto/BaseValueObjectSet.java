package com.canopus.mw.dto;

import java.util.*;

public class BaseValueObjectSet extends BaseValueObject
{
    private static final long serialVersionUID = 6822932493694715438L;
    private Set<? extends BaseValueObject> valueObjectSet;
    
    public BaseValueObjectSet() {
        this.valueObjectSet = null;
    }
    
    public Set<? extends BaseValueObject> getValueObjectSet() {
        return this.valueObjectSet;
    }
    
    public void setValueObjectSet(final Set<? extends BaseValueObject> valueObjectSet) {
        this.valueObjectSet = valueObjectSet;
    }
}

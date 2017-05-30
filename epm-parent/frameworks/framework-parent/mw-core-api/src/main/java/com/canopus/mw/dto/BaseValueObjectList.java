package com.canopus.mw.dto;

import java.util.*;

public class BaseValueObjectList extends BaseValueObject
{
    private static final long serialVersionUID = 6822932493694715438L;
    private List<? extends BaseValueObject> valueObjectList;
    
    public BaseValueObjectList(final List<? extends BaseValueObject> valueObjectList) {
        this.valueObjectList = null;
        this.valueObjectList = valueObjectList;
    }
    
    public BaseValueObjectList() {
        this.valueObjectList = null;
    }
    
    public List<? extends BaseValueObject> getValueObjectList() {
        return this.valueObjectList;
    }
    
    public void setValueObjectList(final List<? extends BaseValueObject> valueObjectList) {
        this.valueObjectList = valueObjectList;
    }
}

package com.canopus.mw.dto;

import java.util.*;

public class BaseValueObjectMap extends BaseValueObject
{
    private static final long serialVersionUID = -2658331181812983196L;
    private Map<? extends BaseValueObject, ? extends BaseValueObject> baseValueMap;
    
    public BaseValueObjectMap() {
        this.baseValueMap = null;
    }
    
    public Map<? extends BaseValueObject, ? extends BaseValueObject> getBaseValueMap() {
        return this.baseValueMap;
    }
    
    public void setBaseValueMap(final Map<? extends BaseValueObject, ? extends BaseValueObject> baseValueMap) {
        this.baseValueMap = baseValueMap;
    }
}

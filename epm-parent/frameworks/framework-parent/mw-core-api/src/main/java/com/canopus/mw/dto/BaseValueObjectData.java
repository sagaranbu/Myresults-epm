package com.canopus.mw.dto;

public class BaseValueObjectData extends BaseValueObject
{
    private static final long serialVersionUID = 6344538393839099702L;
    private Object data;
    
    public Object getData() {
        return this.data;
    }
    
    public void setData(final Object data) {
        this.data = data;
    }
}

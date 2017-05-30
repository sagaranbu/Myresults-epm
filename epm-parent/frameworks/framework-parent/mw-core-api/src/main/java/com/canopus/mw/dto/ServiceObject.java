package com.canopus.mw.dto;

public class ServiceObject extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private Object serviceObject;
    
    public ServiceObject() {
    }
    
    public ServiceObject(final Object serviceObject) {
        this.serviceObject = serviceObject;
    }
    
    public Object getServiceObject() {
        return this.serviceObject;
    }
    
    public void setServiceObject(final Object serviceObject) {
        this.serviceObject = serviceObject;
    }
}

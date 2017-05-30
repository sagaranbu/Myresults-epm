package com.canopus.event.mgr.vo;

public class ServiceRule
{
    private Integer id;
    private String originType;
    private String originId;
    private String originMechanism;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getOriginType() {
        return this.originType;
    }
    
    public void setOriginType(final String originType) {
        this.originType = originType;
    }
    
    public String getOriginId() {
        return this.originId;
    }
    
    public void setOriginId(final String originId) {
        this.originId = originId;
    }
    
    public String getOriginMechanism() {
        return this.originMechanism;
    }
    
    public void setOriginMechanism(final String originMechanism) {
        this.originMechanism = originMechanism;
    }
}

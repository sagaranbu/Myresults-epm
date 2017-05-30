package com.canopus.mw.dto;

public class StatusResponse extends BaseValueObject
{
    private String responseString;
    private String responseValue;
    
    public StatusResponse(final String responseString) {
        this.responseString = responseString;
    }
    
    public StatusResponse(final String responseString, final String responseValue) {
        this.responseString = responseString;
        this.responseValue = responseValue;
    }
    
    public String getResponseString() {
        return this.responseString;
    }
    
    public void setResponseString(final String responseString) {
        this.responseString = responseString;
    }
    
    public String getResponseValue() {
        return this.responseValue;
    }
    
    public void setResponseValue(final String responseValue) {
        this.responseValue = responseValue;
    }
}

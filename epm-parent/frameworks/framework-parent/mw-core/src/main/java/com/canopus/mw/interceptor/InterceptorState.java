package com.canopus.mw.interceptor;

public enum InterceptorState
{
    START("START"), 
    END("END"), 
    ERROR("ERROR");
    
    private String value;
    
    private InterceptorState(final String value) {
        this.value = value;
    }
    
    public String getName() {
        return this.value;
    }
}

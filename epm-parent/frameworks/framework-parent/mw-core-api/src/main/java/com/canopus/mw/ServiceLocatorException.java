package com.canopus.mw;

public class ServiceLocatorException extends RuntimeException
{
    public ServiceLocatorException() {
    }
    
    public ServiceLocatorException(final String arg0) {
        super(arg0);
    }
    
    public ServiceLocatorException(final Throwable arg0) {
        super(arg0);
    }
    
    public ServiceLocatorException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
}

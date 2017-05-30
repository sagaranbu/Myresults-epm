package com.canopus.mw;

public class MiddlewareEventClientException extends RuntimeException
{
    public MiddlewareEventClientException() {
    }
    
    public MiddlewareEventClientException(final String arg0) {
        super(arg0);
    }
    
    public MiddlewareEventClientException(final Throwable arg0) {
        super(arg0);
    }
    
    public MiddlewareEventClientException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
}

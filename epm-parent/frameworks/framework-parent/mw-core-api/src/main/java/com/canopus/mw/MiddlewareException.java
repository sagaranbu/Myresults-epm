package com.canopus.mw;

import javax.ejb.*;
import java.text.*;

@ApplicationException(inherited = true)
public class MiddlewareException extends RuntimeException
{
    private String errCode;
    
    public MiddlewareException(final String errCode, final String message) {
        super(message);
        this.errCode = errCode;
    }
    
    public MiddlewareException(final String errCode, final String message, final Object... params) {
        super(MessageFormat.format(message, params));
        this.errCode = errCode;
    }
    
    public MiddlewareException(final String errCode, final String message, final Throwable root) {
        super(message, root);
        this.errCode = errCode;
    }
    
    public MiddlewareException(final String errCode, final String message, final Throwable root, final Object... params) {
        super(MessageFormat.format(message, params), root);
        this.errCode = errCode;
    }
    
    public String getErrCode() {
        return this.errCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.errCode).append(": ");
        builder.append(super.getMessage());
        return builder.toString();
    }
}

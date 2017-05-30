package com.canopus.dac;

import com.canopus.mw.*;

public class DataAccessException extends MiddlewareException
{
    public DataAccessException(final String errCode, final String message) {
        super(errCode, message);
    }
    
    public DataAccessException(final String errCode, final String message, final Object... params) {
        super(errCode, message, params);
    }
    
    public DataAccessException(final String errCode, final String message, final Throwable root) {
        super(errCode, message, root);
    }
    
    public DataAccessException(final String errCode, final String message, final Throwable root, final Object... params) {
        super(errCode, message, root, params);
    }
}

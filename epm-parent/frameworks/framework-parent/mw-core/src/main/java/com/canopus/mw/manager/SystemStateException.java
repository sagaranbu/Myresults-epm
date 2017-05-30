package com.canopus.mw.manager;

import com.canopus.mw.*;

public class SystemStateException extends MiddlewareException
{
    private static final long serialVersionUID = -7330996772387507048L;
    
    public SystemStateException(final String errCode, final String message) {
        super(errCode, message);
    }
}

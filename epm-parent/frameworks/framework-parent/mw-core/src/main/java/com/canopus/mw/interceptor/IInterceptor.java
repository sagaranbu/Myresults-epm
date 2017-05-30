package com.canopus.mw.interceptor;

import javax.interceptor.*;

public interface IInterceptor
{
    public static final String SYS_STATE_UNHELTY = "SYS-WRIT-00";
    public static final String INV_METHOD_SIGN = "METHOD-SIGN-00";
    
    void start(final InvocationContext p0, final Boolean p1);
    
    void end(final InvocationContext p0, final Boolean p1, final String p2);
    
    void error(final InvocationContext p0, final Boolean p1, final String p2, final Exception p3);
    
    void saveState();
}

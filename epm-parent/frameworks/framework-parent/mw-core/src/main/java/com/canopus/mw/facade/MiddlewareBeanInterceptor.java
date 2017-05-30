package com.canopus.mw.facade;

import com.canopus.mw.*;
import javax.interceptor.*;

public class MiddlewareBeanInterceptor
{
    private MainInterceptor mainInterceptor;
    
    @AroundInvoke
    public Object mwInterceptor(final InvocationContext ctx) throws Exception {
        if (this.mainInterceptor == null && ctx.getTarget() instanceof BaseMiddlewareBean) {
            this.mainInterceptor = ((BaseMiddlewareBean)ctx.getTarget()).getMainInterceptor();
        }
        return this.mainInterceptor.mwInterceptor(ctx);
    }
}

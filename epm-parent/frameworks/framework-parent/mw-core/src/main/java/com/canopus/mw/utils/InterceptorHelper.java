package com.canopus.mw.utils;

import javax.interceptor.*;
import com.canopus.mw.interceptor.*;
import com.canopus.mw.interceptor.param.*;

public class InterceptorHelper
{
    public static String getOperationId(final InvocationContext invContext) {
        String operationId;
        if (invContext.getMethod().getAnnotation(OperationId.class) != null) {
            operationId = invContext.getMethod().getAnnotation(OperationId.class).name();
        }
        else {
            operationId = invContext.getTarget().getClass().getCanonicalName() + "." + invContext.getMethod().getName();
        }
        return operationId;
    }
    
    public static String getOperationMode(final InvocationContext invContext) {
        String mode;
        if (invContext.getMethod().getAnnotation(OperationId.class) != null) {
            mode = invContext.getMethod().getAnnotation(OperationId.class).mode();
        }
        else {
            mode = invContext.getTarget().getClass().getCanonicalName() + "." + invContext.getMethod().getName();
        }
        return mode;
    }
    
    public static boolean canIntercept(final InvocationContext invContext, final Class<? extends IInterceptor> interceptorClass) {
        if (invContext.getMethod().getAnnotation(SkipInterceptors.class) != null) {
            final Class<? extends IInterceptor>[] value;
            final Class<? extends IInterceptor>[] skipInterceptors = value = invContext.getMethod().getAnnotation(SkipInterceptors.class).value();
            for (final Class<? extends IInterceptor> class1 : value) {
                if (class1.getCanonicalName().equals(interceptorClass.getCanonicalName())) {
                    return false;
                }
            }
        }
        return true;
    }
}

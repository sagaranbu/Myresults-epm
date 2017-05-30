package com.canopus.mw.spel;

import java.util.*;
import org.springframework.core.convert.*;
import org.springframework.expression.*;

public class FunctoidMethodResolver implements MethodResolver
{
    Map<String, IExprFunctoid> functoidMap;
    
    public FunctoidMethodResolver(final Map<String, IExprFunctoid> functoidMap) {
        this.functoidMap = null;
        this.functoidMap = functoidMap;
    }
    
    public MethodExecutor resolve(final EvaluationContext context, final Object arg1, final String methodName, final List<TypeDescriptor> arg3) throws AccessException {
        final IExprFunctoid functoid = this.functoidMap.get(methodName);
        final MethodExecutor methodExecutor = (MethodExecutor)new FunctoidMethodExecutor(functoid);
        return methodExecutor;
    }
}

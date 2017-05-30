package com.canopus.mw.spel;

import org.springframework.expression.*;

public class FunctoidMethodExecutor implements MethodExecutor
{
    IExprFunctoid functoid;
    
    public FunctoidMethodExecutor(final IExprFunctoid functoid) {
        this.functoid = null;
        this.functoid = functoid;
    }
    
    public TypedValue execute(final EvaluationContext context, final Object arg1, final Object... params) throws AccessException {
        final Object obj = this.functoid.eval(context, params);
        final TypedValue typedValue = new TypedValue(obj);
        return typedValue;
    }
}

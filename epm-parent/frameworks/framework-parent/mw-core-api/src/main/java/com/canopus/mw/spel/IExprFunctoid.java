package com.canopus.mw.spel;

import org.springframework.expression.*;

public interface IExprFunctoid
{
    String getFunctionName();
    
    Object eval(final EvaluationContext p0, final Object[] p1);
}

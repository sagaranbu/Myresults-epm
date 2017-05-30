package com.canopus.mw.functoids;

import com.canopus.mw.spel.*;
import org.springframework.expression.*;

public class WeightedAverageAggregationFunctoid implements IExprFunctoid
{
    public String getFunctionName() {
        return "wavg";
    }
    
    public Object eval(final EvaluationContext executionContext, final Object[] params) {
        return null;
    }
}

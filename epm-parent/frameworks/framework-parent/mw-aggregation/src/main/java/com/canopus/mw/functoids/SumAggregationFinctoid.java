package com.canopus.mw.functoids;

import com.canopus.mw.spel.*;
import org.springframework.expression.*;
import java.lang.reflect.*;

public class SumAggregationFinctoid implements IExprFunctoid
{
    public String getFunctionName() {
        return "sum";
    }
    
    public Object eval(final EvaluationContext executionContext, final Object[] params) {
        final Object o = executionContext.lookupVariable("root");
        final String sumParamName = (String)params[0];
        final Integer valueToAdd = (Integer)params[1];
        Integer sum1 = 0;
        final Class<?> class1 = o.getClass();
        synchronized (o) {
            try {
                final Field sumField = class1.getField(sumParamName);
                sumField.setAccessible(true);
                Integer sum2 = sumField.getInt(o);
                sum2 += valueToAdd;
                sumField.setInt(o, sum2);
                sumField.setAccessible(false);
                sum1 = sum2;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum1;
    }
}

package com.canopus.mw.functoids;

import com.canopus.mw.spel.*;
import org.springframework.expression.*;
import java.lang.reflect.*;

public class AverageAggegationFunctoid implements IExprFunctoid
{
    public String getFunctionName() {
        return "avg";
    }
    
    public Object eval(final EvaluationContext executionContext, final Object[] params) {
        final Integer valueToAdd = (Integer)params[0];
        final String countParamName = (String)params[1];
        final String avgParamName = (String)params[2];
        final Object o = executionContext.lookupVariable("root");
        int avg1 = 0;
        final Class<?> class1 = o.getClass();
        try {
            synchronized (o) {
                final Field countField = class1.getDeclaredField(countParamName);
                countField.setAccessible(true);
                final Field avgField = class1.getDeclaredField(avgParamName);
                avgField.setAccessible(true);
                final int count = countField.getInt(o);
                int avg2 = avgField.getInt(o);
                avg2 = valueToAdd + avg2 * count / count + 1;
                avgField.setInt(o, avg2);
                avg1 = avg2;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return avg1;
    }
}

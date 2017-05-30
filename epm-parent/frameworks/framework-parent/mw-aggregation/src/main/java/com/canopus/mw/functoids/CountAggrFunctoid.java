package com.canopus.mw.functoids;

import com.canopus.mw.spel.*;
import org.springframework.expression.*;
import java.lang.reflect.*;

public class CountAggrFunctoid implements IExprFunctoid
{
    public String getFunctionName() {
        return "count";
    }
    
    public Object eval(final EvaluationContext executionContext, final Object[] params) {
        final Object o = executionContext.lookupVariable("root");
        final String propertyName = (String)params[0];
        Integer data = new Integer(0);
        final Class<?> class1 = o.getClass();
        try {
            final Field field = class1.getDeclaredField(propertyName);
            field.setAccessible(true);
            synchronized (o) {
                data = field.getInt(o);
                final Boolean condation = (Boolean)params[1];
                if (condation) {
                    ++data;
                    field.set(o, data);
                }
                else {
                    --data;
                    field.set(o, data);
                }
            }
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (SecurityException e2) {
            e2.printStackTrace();
        }
        catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        }
        catch (IllegalAccessException e4) {
            e4.printStackTrace();
        }
        return data;
    }
}

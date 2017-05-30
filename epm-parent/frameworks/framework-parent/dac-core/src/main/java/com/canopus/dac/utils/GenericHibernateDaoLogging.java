package com.canopus.dac.utils;

import org.apache.log4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.reflect.*;
import org.perf4j.log4j.*;
import java.lang.reflect.*;
import org.perf4j.*;

public class GenericHibernateDaoLogging
{
    Logger log;
    
    public GenericHibernateDaoLogging() {
        this.log = Logger.getLogger((Class)GenericHibernateDaoLogging.class);
    }
    
    public Object log(final ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature)pjp.getSignature();
        final Method method = signature.getMethod();
        final StringBuilder tagName = new StringBuilder(pjp.getTarget().getClass().getSimpleName());
        tagName.append(".").append(method.getName());
        final StopWatch stopWatch = (StopWatch)new Log4JStopWatch("GHDAO-" + tagName.toString());
        try {
            return pjp.proceed(pjp.getArgs());
        }
        finally {
            stopWatch.stop();
        }
    }
}

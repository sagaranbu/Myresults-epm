package com.canopus.dac.utils;

import org.apache.log4j.*;
import org.aspectj.lang.reflect.*;
import org.perf4j.log4j.*;
import java.lang.reflect.*;
import org.perf4j.*;
import org.aspectj.lang.*;

public class Perf4jDACLogging
{
    Logger log;
    
    public Perf4jDACLogging() {
        this.log = Logger.getLogger((Class)Perf4jDACLogging.class);
    }
    
    public Object log(final ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature)pjp.getSignature();
        final Method method = signature.getMethod();
        final StringBuilder tagName = new StringBuilder(pjp.getTarget().getClass().getSimpleName());
        tagName.append(".").append(method.getName());
        final StopWatch stopWatch = (StopWatch)new Log4JStopWatch("DAC-" + tagName.toString());
        try {
            return pjp.proceed(pjp.getArgs());
        }
        finally {
            stopWatch.stop();
        }
    }
    
    public void before(final JoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature)pjp.getSignature();
        final Method method = signature.getMethod();
        final StringBuilder tagName = new StringBuilder(pjp.getTarget().getClass().getSimpleName());
        tagName.append(".").append(method.getName());
        System.out.println((Object)tagName + "-Before");
    }
    
    public void after(final JoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature)pjp.getSignature();
        final Method method = signature.getMethod();
        final StringBuilder tagName = new StringBuilder(pjp.getTarget().getClass().getSimpleName());
        tagName.append(".").append(method.getName());
        System.out.println((Object)tagName + "-After");
        Thread.dumpStack();
    }
}

package com.canopus.mw.facade;

import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import java.util.concurrent.*;
import javax.interceptor.*;
import javax.jms.*;
import com.canopus.mw.*;
import com.canopus.mw.events.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.interceptor.*;
import java.util.*;
import javax.annotation.*;

public class MainInterceptor
{
    @Autowired
    List<IInterceptor> interceptors;
    IInterceptor[] interceptorsArr;
    private static Logger logger;
    private HashSet<String> preInvocationMethods;
    private HashSet<String> postInvocationMethods;
    ExecutorService executorService;
    
    public void setPreInvocationMethods(final String preInvocationMethods) {
        if (preInvocationMethods != null && preInvocationMethods.length() > 0) {
            this.preInvocationMethods.addAll(Arrays.asList(preInvocationMethods.split(",")));
        }
    }
    
    public void setPostInvocationMethods(final String postInvocationMethods) {
        if (postInvocationMethods != null && postInvocationMethods.length() > 0) {
            this.postInvocationMethods.addAll(Arrays.asList(postInvocationMethods.split(",")));
        }
    }
    
    public MainInterceptor(final String interval) {
        this.preInvocationMethods = new HashSet<String>();
        this.postInvocationMethods = new HashSet<String>();
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    @PostConstruct
    public void init() {
        MainInterceptor.logger.debug((Object)"Initializing main interceptor");
        if (this.interceptors != null) {
            final int count = this.interceptors.size();
            this.interceptorsArr = this.interceptors.toArray(new IInterceptor[count]);
        }
    }
    
    public Object mwInterceptor(final InvocationContext ctx) throws Exception {
        Map<String, Object> context = null;
        Object obj = null;
        final String requestPath = null;
        final String reqID = ctx.getMethod().getName();
        if (ctx.getMethod().getName().equals("getServiceId")) {
            return ctx.proceed();
        }
        final long entryTS = System.currentTimeMillis();
        ExecutionContext.getCurrent().push(ctx.getMethod().getName());
        Object param = null;
        long notStartTS;
        long notEndTS;
        long procEndTS;
        long postNotEndTS;
        try {
            final Object[] params = ctx.getParameters();
            if (params.length > 0 && params[0] instanceof Request) {
                final Request request = (Request)params[0];
                context = (Map<String, Object>)request.getContext();
                param = request;
                ExecutionContext.getCurrent().setContextValues((Map)context);
            }
            else {
                if (params.length <= 0 || !(params[0] instanceof ObjectMessage)) {
                    throw new MiddlewareException("METHOD-SIGN-00", "Parameter request not found.");
                }
                final Object o = ((ObjectMessage)params[0]).getObject();
                if (o instanceof MiddlewareEvent) {
                    final MiddlewareEvent event = (MiddlewareEvent)((ObjectMessage)params[0]).getObject();
                    context = (Map<String, Object>)event.getContext();
                    param = event;
                    ExecutionContext.getCurrent().setContextValues((Map)context);
                }
                else if (o instanceof MiddlewareEventBean) {
                    final MiddlewareEventBean event2 = (MiddlewareEventBean)((ObjectMessage)params[0]).getObject();
                    context = (Map<String, Object>)event2.getContext();
                    param = event2;
                    ExecutionContext.getCurrent().setContextValues((Map)context);
                }
            }
            ExecutionContext.getCurrent().getResponse();
            Boolean isEntryPoint;
            if (ExecutionContext.getCurrent().getContextValues().containsKey(HeaderParam.REQUEST_ID.getParamName())) {
                isEntryPoint = false;
            }
            else {
                isEntryPoint = true;
            }
            notStartTS = System.currentTimeMillis();
            this.notifyInterceptor(ctx, InterceptorState.START, isEntryPoint, null, null);
            notEndTS = System.currentTimeMillis();
            try {
                obj = ctx.proceed();
                procEndTS = System.currentTimeMillis();
                this.notifyInterceptor(ctx, InterceptorState.END, isEntryPoint, requestPath, null);
                postNotEndTS = System.currentTimeMillis();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this.notifyInterceptor(ctx, InterceptorState.ERROR, isEntryPoint, requestPath, exception);
                if (exception instanceof MiddlewareException) {
                    throw exception;
                }
                throw new MiddlewareException("MINT-000", "Middleware Service should throw only MiddlewareException because, MiddlewareException is only the application exception", (Throwable)exception);
            }
        }
        finally {
            ExecutionContext.getCurrent().cleanup();
        }
        final long exitTS = System.currentTimeMillis();
        MainInterceptor.logger.debug((Object)("Processing done, returning - " + reqID + " entry=" + entryTS + ", notStartTS=" + notStartTS + ", notEndTS=" + notEndTS + ", procEndTS=" + procEndTS + ", postNotEndTS=" + postNotEndTS + ", exitTS=" + exitTS));
        return obj;
    }
    
    private void fireOperationInvocationEvent(final Object param, final String operationId, final String invocationType) {
    }
    
    private void notifyInterceptor(final InvocationContext ctx, final InterceptorState state, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
        for (int i = 0; i < this.interceptorsArr.length; ++i) {
            final IInterceptor interceptor = this.interceptorsArr[i];
            final long beg = System.currentTimeMillis();
            switch (state) {
                case START: {
                    interceptor.start(ctx, isEntryPoint);
                    break;
                }
                case END: {
                    interceptor.end(ctx, isEntryPoint, requestPath);
                    break;
                }
                case ERROR: {
                    interceptor.error(ctx, isEntryPoint, requestPath, exception);
                    break;
                }
            }
            System.currentTimeMillis();
        }
    }
    
    private void notifySaveState() {
        for (final IInterceptor interceptor : this.interceptors) {
            interceptor.saveState();
        }
    }
    
    @PreDestroy
    public void destroy() throws Exception {
        this.executorService.shutdown();
    }
    
    static {
        MainInterceptor.logger = Logger.getLogger((Class)MainInterceptor.class);
    }
}

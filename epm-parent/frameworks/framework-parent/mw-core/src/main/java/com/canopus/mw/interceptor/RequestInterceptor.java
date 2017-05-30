package com.canopus.mw.interceptor;

import org.springframework.stereotype.*;
import com.canopus.mw.manager.*;
import org.springframework.beans.factory.annotation.*;
import java.util.concurrent.atomic.*;
import javax.interceptor.*;
import com.canopus.mw.dto.param.*;
import org.apache.log4j.*;
import com.canopus.mw.dto.*;

@Component
public class RequestInterceptor implements IInterceptor
{
    @Autowired
    private ComponentManager componentManager;
    private Logger logger;
    private static AtomicInteger sequenceNumber;
    
    public RequestInterceptor() {
        this.logger = Logger.getLogger((Class)RequestInterceptor.class);
    }
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        final ComponentInfo componentInfo = this.componentManager.getComponentInfo();
        if (!ExecutionContext.getCurrent().getContextValues().containsKey(HeaderParam.REQUEST_ID.getParamName())) {
            String REQUEST_ID = "";
            String REQUEST_PATH = "";
            final int seqnum = RequestInterceptor.sequenceNumber.incrementAndGet();
            REQUEST_ID = componentInfo.getName() + "." + componentInfo.getInstanceID() + "." + seqnum;
            ExecutionContext.getCurrent().getContextValues().put(HeaderParam.REQUEST_ID.getParamName(), REQUEST_ID);
            REQUEST_PATH = REQUEST_ID + "/" + componentInfo.getName() + "." + componentInfo.getInstanceID() + "." + invContext.getMethod().getName();
            ExecutionContext.getCurrent().getContextValues().put(HeaderParam.REQUEST_PATH.getParamName(), REQUEST_PATH);
            final Object[] params = invContext.getParameters();
            if (params.length > 0 && params[0] instanceof Request) {
                final Request req = (Request)params[0];
                req.getContext().put(HeaderParam.REQUEST_ID.getParamName(), REQUEST_ID);
                req.getContext().put(HeaderParam.REQUEST_PATH.getParamName(), REQUEST_PATH);
                params[0] = req;
                invContext.setParameters(params);
            }
            MDC.put("requestID", (Object)REQUEST_ID);
        }
        else {
            final String REQUEST_PATH2 = ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()) + "/" + componentInfo.getName() + "." + componentInfo.getInstanceID() + "." + invContext.getMethod().getName();
            ExecutionContext.getCurrent().getContextValues().put(HeaderParam.REQUEST_PATH.getParamName(), REQUEST_PATH2);
        }
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
    }
    
    @Override
    public void saveState() {
    }
    
    static {
        RequestInterceptor.sequenceNumber = new AtomicInteger();
    }
}

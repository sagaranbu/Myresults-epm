package com.canopus.mw.interceptor;

import com.canopus.interceptor.vo.*;
import org.apache.log4j.*;
import com.canopus.interceptor.*;
import org.springframework.beans.factory.annotation.*;
import javax.interceptor.*;
import com.canopus.mw.utils.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.util.*;

public class OperationProfilerInterceptor implements IInterceptor
{
    List<OperationProfilerData> operationProfilerQueue;
    private static Logger logger;
    private boolean dbLoggingEnabled;
    @Autowired
    private InterceptorDataService interceptorDataService;
    private static ThreadLocal<Stack<OperationExecutionTime>> exetimeStack;
    
    public OperationProfilerInterceptor() {
        this.operationProfilerQueue = new ArrayList<OperationProfilerData>();
        this.dbLoggingEnabled = false;
        this.interceptorDataService = null;
    }
    
    public static Stack<OperationExecutionTime> getCurrent() {
        return OperationProfilerInterceptor.exetimeStack.get();
    }
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        final String operationId = InterceptorHelper.getOperationId(invContext);
        OperationProfilerInterceptor.logger.info((Object)("OperationProfilerInterceptor Created OperationID: " + operationId));
        getCurrent().push(new OperationExecutionTime(operationId, (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()), new Date()));
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
        final OperationExecutionTime oeTime = getCurrent().pop();
        final Date currentTime = new Date();
        final Long operationExeTime = currentTime.getTime() - oeTime.getStartTime().getTime();
        OperationProfilerInterceptor.logger.debug((Object)("Execution Time: " + operationExeTime + " for OperationId: " + oeTime.getOperationId() + " $$ REQUEST_PATH: " + oeTime.getRequestPath() + " $$ Stack Size: " + getCurrent().size()));
        if (this.dbLoggingEnabled) {
            final OperationProfilerData operationProfilerData = this.getOperationProfilerData(oeTime.getOperationId(), oeTime.getRequestPath(), oeTime.getStartTime(), currentTime, operationExeTime);
            synchronized (this) {
                this.operationProfilerQueue.add(operationProfilerData);
            }
        }
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
        final OperationExecutionTime oeTime = getCurrent().pop();
        final Date currentTime = new Date();
        final Long operationExeTime = currentTime.getTime() - oeTime.getStartTime().getTime();
        OperationProfilerInterceptor.logger.debug((Object)("Error: Execution Time: " + operationExeTime + " for OperationId: " + oeTime.getOperationId() + " $$ REQUEST_PAHT: " + oeTime.getRequestPath() + " $$ ErrorMessage: " + exception.getMessage()));
        if (this.dbLoggingEnabled) {
            final OperationProfilerData operationProfilerData = this.getOperationProfilerData(oeTime.getOperationId(), oeTime.getRequestPath(), oeTime.getStartTime(), currentTime, operationExeTime);
            synchronized (this) {
                this.operationProfilerQueue.add(operationProfilerData);
            }
        }
    }
    
    private OperationProfilerData getOperationProfilerData(final String operationId, final String requestPath, final Date startTime, final Date endTime, final long executionTime) {
        final String requestId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName());
        final String userId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_NAME.getParamName());
        final String proxyUserId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.PROXY_USER_ID.getParamName());
        final Integer tenantId = ExecutionContext.getTenantId();
        final OperationProfilerData operationProfilerData = new OperationProfilerData((Integer)null, tenantId, operationId, requestPath, requestId, startTime, endTime, executionTime, userId, proxyUserId);
        return operationProfilerData;
    }
    
    @Override
    public void saveState() {
        final List<OperationProfilerData> list;
        synchronized (this) {
            list = new ArrayList<OperationProfilerData>(this.operationProfilerQueue);
            this.operationProfilerQueue.clear();
        }
        this.interceptorDataService.saveOperationProfilerList((List)list);
    }
    
    static {
        OperationProfilerInterceptor.logger = Logger.getLogger((Class)OperationProfilerInterceptor.class);
        OperationProfilerInterceptor.exetimeStack = new ThreadLocal<Stack<OperationExecutionTime>>() {
            @Override
            protected Stack<OperationExecutionTime> initialValue() {
                final Stack<OperationExecutionTime> exetimeStack = new Stack<OperationExecutionTime>();
                return exetimeStack;
            }
        };
    }
    
    public class OperationExecutionTime
    {
        private String operationId;
        private String requestPath;
        private Date startTime;
        
        public OperationExecutionTime(final String operationId, final String requestPath, final Date startTime) {
            this.operationId = operationId;
            this.requestPath = requestPath;
            this.startTime = startTime;
        }
        
        public String getOperationId() {
            return this.operationId;
        }
        
        public void setOperationId(final String operationId) {
            this.operationId = operationId;
        }
        
        public String getRequestPath() {
            return this.requestPath;
        }
        
        public void setRequestPath(final String requestPath) {
            this.requestPath = requestPath;
        }
        
        public Date getStartTime() {
            return this.startTime;
        }
        
        public void setStartTime(final Date startTime) {
            this.startTime = startTime;
        }
    }
}

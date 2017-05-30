package com.canopus.mw.interceptor;

import org.apache.log4j.*;
import com.canopus.interceptor.vo.*;
import com.canopus.interceptor.*;
import org.springframework.beans.factory.annotation.*;
import javax.interceptor.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import org.codehaus.jackson.map.*;
import java.util.*;

public class AuditLoggingInterceptor implements IInterceptor
{
    public Logger logger;
    LinkedHashMap<String, AuditLoggingData> auditLoggingQueue;
    @Autowired
    private InterceptorDataService interceptorDataService;
    private boolean dbLoggingEnabled;
    
    public AuditLoggingInterceptor() {
        this.logger = Logger.getLogger((Class)AuditLoggingInterceptor.class);
        this.auditLoggingQueue = new LinkedHashMap<String, AuditLoggingData>();
        this.interceptorDataService = null;
        this.dbLoggingEnabled = false;
    }
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        this.logger.debug((Object)(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName()) + ", " + ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()) + ", " + invContext.getMethod().getName() + ", " + InterceptorState.START.getName()));
        if (this.dbLoggingEnabled) {
            final Object[] params = invContext.getParameters();
            final ObjectMapper objectMapper = new ObjectMapper();
            this.logger.trace((Object)(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName()) + ", " + ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()) + ", " + invContext.getMethod().getName() + ", " + InterceptorState.START.getName() + ", "));
            if (isEntryPoint) {
                final String requestId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName());
                final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.getParamName());
                final AuditLoggingData auditLoggingData = new AuditLoggingData((Integer)null, tenantId, requestId, InterceptorState.START.getName(), new Date(), (Date)null);
                synchronized (this) {
                    this.auditLoggingQueue.put(requestId, auditLoggingData);
                }
            }
        }
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
        this.logger.debug((Object)(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName()) + ", " + ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()) + ", " + invContext.getMethod().getName() + ", " + InterceptorState.END.getName()));
        if (this.dbLoggingEnabled && isEntryPoint) {
            final String requestId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName());
            synchronized (this) {
                final AuditLoggingData auditLoggingData = this.auditLoggingQueue.get(requestId);
                auditLoggingData.setRequestState(InterceptorState.END.getName());
                auditLoggingData.setEndTime(new Date());
                this.auditLoggingQueue.put(requestId, auditLoggingData);
            }
        }
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
        this.logger.warn((Object)(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName()) + ", " + ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_PATH.getParamName()) + ", " + invContext.getMethod().getName() + ", " + InterceptorState.ERROR.getName() + ", ErrorMessage:" + exception.getMessage()));
        if (this.dbLoggingEnabled && isEntryPoint) {
            final String requestId = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName());
            synchronized (this) {
                final AuditLoggingData auditLoggingData = this.auditLoggingQueue.get(requestId);
                auditLoggingData.setRequestState(InterceptorState.ERROR.getName());
                auditLoggingData.setEndTime(new Date());
                this.auditLoggingQueue.put(requestId, auditLoggingData);
            }
        }
    }
    
    @Override
    public void saveState() {
        final Map<String, AuditLoggingData> map;
        synchronized (this) {
            map = new LinkedHashMap<String, AuditLoggingData>(this.auditLoggingQueue);
            for (final Map.Entry mapEntry : map.entrySet()) {
                final AuditLoggingData auditLoggingData = (AuditLoggingData) mapEntry.getValue();
                if (!auditLoggingData.getRequestState().equals(InterceptorState.START.getName())) {
                    this.auditLoggingQueue.remove(mapEntry.getKey());
                }
            }
        }
        final Map<String, AuditLoggingData> updatedAuditLogMap = (Map<String, AuditLoggingData>)this.interceptorDataService.saveRequstStatsList((Map)map);
        synchronized (this) {
            for (final Map.Entry mapEntry2 : updatedAuditLogMap.entrySet()) {
                final AuditLoggingData updatedAuditLoggingData = (AuditLoggingData) mapEntry2.getValue();
                if (updatedAuditLoggingData.getRequestState().equals(InterceptorState.START.getName())) {
                    final AuditLoggingData queueAuditLoggingData = this.auditLoggingQueue.get(mapEntry2.getKey());
                    queueAuditLoggingData.setId(updatedAuditLoggingData.getId());
                    this.auditLoggingQueue.put((String) mapEntry2.getKey(), queueAuditLoggingData);
                }
            }
        }
    }
}

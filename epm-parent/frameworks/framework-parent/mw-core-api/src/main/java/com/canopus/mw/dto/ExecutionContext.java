package com.canopus.mw.dto;

import java.util.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.*;

public class ExecutionContext
{
    private static final Integer SYSTEM_TENANT_ID;
    private static ThreadLocal<ExecutionContext> context;
    Stack<String> serviceCallStack;
    private Map<String, Map<String, Object>> contextStackValues;
    
    public ExecutionContext() {
        this.serviceCallStack = new Stack<String>();
        this.contextStackValues = new HashMap<String, Map<String, Object>>();
    }
    
    public static ExecutionContext getCurrent() {
        return ExecutionContext.context.get();
    }
    
    public Map<String, Object> getContextValues() {
        final String serviceCallStack = this.getServiceCallStack();
        Map<String, Object> contextValues = this.contextStackValues.get(serviceCallStack);
        if (contextValues == null) {
            contextValues = new HashMap<String, Object>();
            this.setContextValues(contextValues, serviceCallStack);
        }
        return this.contextStackValues.get(serviceCallStack);
    }
    
    public void setContextValues(final Map<String, Object> currentContextValues) {
        this.contextStackValues.put(this.getServiceCallStack(), new HashMap<String, Object>(currentContextValues));
    }
    
    public void setContextValues(final Map<String, Object> currentContextValues, final String serviceCallStack) {
        this.contextStackValues.put(serviceCallStack, new HashMap<String, Object>(currentContextValues));
    }
    
    public void removeContext() {
        this.contextStackValues.remove(this.getServiceCallStack());
    }
    
    public void cleanup() {
        this.removeContext();
        this.pop();
    }
    
    public Response getResponse() {
        Response contextResponse = (Response) getCurrent().getContextValues().get("RESPONSE");
        if (contextResponse == null) {
            contextResponse = new Response();
            getCurrent().getContextValues().put("RESPONSE", contextResponse);
        }
        return contextResponse;
    }
    
    public void push(final String methodName) {
        this.serviceCallStack.push(methodName);
    }
    
    public String pop() {
        return this.serviceCallStack.pop();
    }
    
    public String peek() {
        return this.serviceCallStack.peek();
    }
    
    public String getServiceCallStack() {
        String serviceCallPath = "";
        for (final String value : this.serviceCallStack) {
            if (serviceCallPath.equals("")) {
                serviceCallPath = value;
            }
            else {
                serviceCallPath = serviceCallPath + "/" + value;
            }
        }
        if (serviceCallPath.equals("")) {
            this.serviceCallStack.push("default");
            return "default";
        }
        return serviceCallPath;
    }
    
    public void setCrossTenant() {
        final Map<String, Object> contextValues = getCurrent().getContextValues();
        if (contextValues != null) {
            getCurrent().getContextValues().put(HeaderParam.CROSS_TENANT.getParamName(), true);
            final Integer tenantId = (Integer) contextValues.get(HeaderParam.TENANT_ID.getParamName());
            if (tenantId == null) {
                contextValues.put(HeaderParam.TENANT_ID.getParamName(), getSystemTenantId());
            }
        }
    }
    
    public void unSetCrossTenant() {
        final Map<String, Object> contextValues = getCurrent().getContextValues();
        if (contextValues != null) {
            getCurrent().getContextValues().put(HeaderParam.CROSS_TENANT.getParamName(), false);
        }
    }
    
    public boolean isCrossTenant() {
        return getCurrent().getContextValues().containsKey(HeaderParam.CROSS_TENANT.getParamName()) && ((Boolean)getCurrent().getContextValues().get(HeaderParam.CROSS_TENANT.getParamName()));
    }
    
    public static Integer getTenantId() {
        final ExecutionContext context = getCurrent();
        final Map<String, Object> contextValues = context.getContextValues();
        final Integer tenantId = (Integer) contextValues.get(HeaderParam.TENANT_ID.getParamName());
        if (tenantId == null) {
            throw new MiddlewareException("CXT-NO-TENANT-00", "No tenant id set to the context.");
        }
        return tenantId;
    }
    
    public static void setTenantId(final Integer tenantId) {
        final ExecutionContext context = getCurrent();
        final Map<String, Object> contextValues = context.getContextValues();
        contextValues.put(HeaderParam.TENANT_ID.getParamName(), tenantId);
    }
    
    public static Integer getSystemTenantId() {
        return ExecutionContext.SYSTEM_TENANT_ID;
    }
    
    static {
        SYSTEM_TENANT_ID = -1;
        ExecutionContext.context = new ThreadLocal<ExecutionContext>() {
            @Override
            protected ExecutionContext initialValue() {
                final ExecutionContext context = new ExecutionContext();
                return context;
            }
        };
    }
}

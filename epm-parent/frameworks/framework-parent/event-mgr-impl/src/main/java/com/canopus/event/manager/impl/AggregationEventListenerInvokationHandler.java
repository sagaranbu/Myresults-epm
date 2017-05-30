package com.canopus.event.manager.impl;

import com.canopus.event.mgr.*;
import com.canopus.mw.*;
import com.canopus.event.mgr.vo.*;
import com.canopus.mw.events.*;
import com.canopus.mw.aggregation.*;
import com.canopus.mw.dto.*;

public class AggregationEventListenerInvokationHandler implements IEventListenerInvokationHandler
{
    private AggregationRule aggregationRule;
    private IServiceLocator serviceLocator;
    private String type;
    private String invokeMechanism;
    private EventOriginData eventOriginData;
    private EventData eventData;
    
    public AggregationEventListenerInvokationHandler() {
    }
    
    public AggregationEventListenerInvokationHandler(final AggregationRule aggregationRule, final IServiceLocator serviceLocator, final String type, final String invokeMechanism) {
        this.aggregationRule = aggregationRule;
        this.serviceLocator = serviceLocator;
        this.type = type;
        this.invokeMechanism = invokeMechanism;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getInvokeMechanism() {
        return this.invokeMechanism;
    }
    
    public void setInvokeMechanism(final String invokeMechanism) {
        this.invokeMechanism = invokeMechanism;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String invokeMechanism() {
        return this.invokeMechanism;
    }
    
    public String invoke(final MiddlewareEvent middlewareEvent) {
        return "sucess";
    }
    
    public IServiceLocator getServiceLocator() {
        return this.serviceLocator;
    }
    
    public void setServiceLocator(final IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }
    
    public AggregationRule getAggregationRule() {
        return this.aggregationRule;
    }
    
    public void setAggregationRule(final AggregationRule aggregationRule) {
        this.aggregationRule = aggregationRule;
    }
    
    public EventOriginData getEventOriginData() {
        if (this.eventOriginData == null) {
            final String[] arr = this.aggregationRule.getOriginId().split(".");
            if (arr.length < 2) {
                return null;
            }
            this.eventOriginData = new EventOriginData(arr[0], arr[1], this.aggregationRule.getOriginId());
        }
        return this.eventOriginData;
    }
    
    public String invoke(final MiddlewareEventBean middlewareEventBean) {
        if (this.aggregationRule.isDisabled()) {
            return "";
        }
        try {
            final Request request = new Request();
            final IAggregationService service = (IAggregationService)this.serviceLocator.getService("UMSAggregationService");
            request.put("aggregationRule", (BaseValueObject)this.aggregationRule);
            request.put("aggregationData", (BaseValueObject)middlewareEventBean);
            service.aggregate(request);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "sucess";
    }
    
    public EventData getEventData() {
        if (null == this.eventData) {
            (this.eventData = new EventData()).setEventId(this.aggregationRule.getOriginId());
            this.eventData.setEventOriginMechanism(this.aggregationRule.getOriginMechanism());
        }
        return this.eventData;
    }
}

package com.canopus.event.manager.impl;

import com.canopus.event.mgr.*;
import com.canopus.event.mgr.vo.*;
import com.canopus.mw.events.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.apache.camel.*;

public class NotificationEventListnerInvocationHandler implements IEventListenerInvokationHandler
{
    private String type;
    private String invokeMechanism;
    private EventOriginData eventOriginData;
    private EventData eventData;
    private NotificationRuleData notificationRuleData;
    private CamelContext camelContext;
    
    public NotificationEventListnerInvocationHandler(final CamelContext camelContext, final EventData eventData, final NotificationRuleData notificationRuleData, final String type, final String invokeMechanism) {
        this.camelContext = camelContext;
        this.type = type;
        this.invokeMechanism = invokeMechanism;
        this.eventData = eventData;
        this.notificationRuleData = notificationRuleData;
    }
    
    public String getType() {
        return this.type;
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
    
    public EventOriginData getEventOriginData() {
        if (this.eventOriginData == null) {
            if (this.eventData.getEventOriginMechanism().equals("Contextual")) {
                this.eventOriginData = new EventOriginData((String)null, this.eventData.getEventId(), this.eventData.getEventOriginMechanism());
            }
            else {
                this.eventOriginData = new EventOriginData((String)null, this.eventData.getEventId(), this.eventData.getEventOriginMechanism());
            }
        }
        return this.eventOriginData;
    }
    
    public void setEventOriginData(final EventOriginData eventOriginData) {
        this.eventOriginData = eventOriginData;
    }
    
    public String invokeMechanism() {
        return this.invokeMechanism;
    }
    
    public EventData getEventData() {
        return this.eventData;
    }
    
    public void setEventData(final EventData eventData) {
        this.eventData = eventData;
    }
    
    public NotificationRuleData getNotificationRuleData() {
        return this.notificationRuleData;
    }
    
    public void setNotificationRuleData(final NotificationRuleData notificationRuleData) {
        this.notificationRuleData = notificationRuleData;
    }
    
    public CamelContext getCamelContext() {
        return this.camelContext;
    }
    
    public void setCamelContext(final CamelContext camelContext) {
        this.camelContext = camelContext;
    }
    
    public String invoke(final MiddlewareEvent middlewareEvent) {
        return null;
    }
    
    public String invoke(final MiddlewareEventBean middlewareEventBean) {
        if (this.notificationRuleData.getIsDisabled()) {
            return "";
        }
        final ProducerTemplate template = this.camelContext.createProducerTemplate();
        final Map<String, Object> headers = new HashMap<String, Object>();
        if (this.notificationRuleData.getNotificationChannel().equals("Email")) {
            headers.put("notificationRule", this.notificationRuleData);
            headers.put("middlewareEventBean", middlewareEventBean);
            headers.put("Subject", this.notificationRuleData.getSubjectTemplate());
            template.requestBodyAndHeaders("direct:email", (Object)this.notificationRuleData.getMessageTemplate(), (Map)headers);
        }
        else if (this.notificationRuleData.getNotificationChannel().equals("Dashboard")) {
            headers.put("notificationRule", this.notificationRuleData);
            headers.put("middlewareEventBean", middlewareEventBean);
            headers.put("Subject", this.notificationRuleData.getSubjectTemplate());
            template.requestBodyAndHeaders("direct:dashboard", (Object)this.notificationRuleData.getMessageTemplate(), (Map)headers);
        }
        return null;
    }
}

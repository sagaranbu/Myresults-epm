package com.canopus.event.manager.impl;

import javax.ejb.*;
import org.jboss.ejb3.annotation.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.event.mgr.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.*;
import javax.jms.*;
import com.canopus.mw.events.*;
import java.util.*;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "GlobalEvent") })
@ResourceAdapter("activemq.rar")
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class AsyncEventManager extends BaseMiddlewareEventListener implements MessageListener
{
    @Value("#{eventInvocationMap}")
    private Map<String, List<IEventListenerInvokationHandler>> eventInvocationMap;
    
    public void onMessage(final Message message) {
        try {
            final Object o = ((ObjectMessage)message).getObject();
            this.handleEvent((MiddlewareEventBean)o);
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public void handleEvent(final MiddlewareEvent event) {
    }
    
    public void handleEvent(final MiddlewareEventBean eventBean) {
        final List<IEventListenerInvokationHandler> eventListenerInvokationHandlers = this.eventInvocationMap.get(eventBean.getEventType().trim());
        for (final IEventListenerInvokationHandler i : eventListenerInvokationHandlers) {
            i.invoke(eventBean);
        }
    }
}

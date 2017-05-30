package com.canopus.event.manager.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import java.util.logging.*;
import com.canopus.event.mgr.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.events.*;
import org.apache.activemq.*;
import java.io.*;
import javax.jms.*;
import javax.jms.Message;

import com.canopus.event.mgr.vo.*;
import com.canopus.event.mgr.vo.params.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ IEventManager.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class EventManagerServiceImple extends BaseMiddlewareBean implements IEventManager
{
    Logger log;
    @Value("#{eventInvocationMap}")
    private Map<String, List<IEventListenerInvokationHandler>> eventInvocationMap;
    @Autowired
    private NotificationRuleDomainManagerImpl notificationDomainManager;
    private String jmsProviderURL;
    
    public EventManagerServiceImple() {
        this.log = Logger.getLogger(EventManagerServiceImple.class.getName());
        this.jmsProviderURL = "tcp://localhost:61616";
    }
    
    public String getJmsProviderURL() {
        return this.jmsProviderURL;
    }
    
    public void setJmsProviderURL(final String jmsProviderURL) {
        this.jmsProviderURL = jmsProviderURL;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response fireEvent(final Request request) {
        try {
            final Object event1 = request.get("EVENT");
            if (event1.getClass().equals(MiddlewareEvent.class)) {
                final MiddlewareEvent event2 = (MiddlewareEvent)event1;
                final List<IEventListenerInvokationHandler> eventListenerInvokationHandlers = this.eventInvocationMap.get(event2.getEventType());
                for (final IEventListenerInvokationHandler i : eventListenerInvokationHandlers) {
                    if (i.invokeMechanism().trim().equals("sync")) {
                        i.invoke(event2);
                    }
                    else {
                        if (!i.invokeMechanism().trim().equals("async")) {
                            continue;
                        }
                        this.fireAsyncEvent(event2);
                    }
                }
            }
            else if (event1.getClass().equals(MiddlewareEventBean.class)) {
                System.err.println("processing " + MiddlewareEventBean.class + "..........");
                final MiddlewareEventBean eventBean = (MiddlewareEventBean)event1;
                final List<IEventListenerInvokationHandler> eventListenerInvokationHandlers2 = this.eventInvocationMap.get(eventBean.getEventType().trim());
                for (final IEventListenerInvokationHandler j : eventListenerInvokationHandlers2) {
                    if (j.invokeMechanism().trim().equals("sync")) {
                        j.invoke(eventBean);
                    }
                    else {
                        if (!j.invokeMechanism().trim().equals("async")) {
                            continue;
                        }
                        this.fireAsyncEvent(eventBean);
                    }
                }
            }
        }
        catch (Exception e) {
            return this.ERROR(e);
        }
        return this.OK();
    }
    
    public void fireAsyncEvent(final MiddlewareEventBean event) {
        final boolean persistent = false;
        final int timeToLive = 0;
        Connection connection = null;
        try {
            final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.jmsProviderURL);
            connection = connectionFactory.createConnection();
            connection.start();
            final Session session = connection.createSession(false, 1);
            final Destination destination = (Destination)session.createTopic("GlobalEvent");
            final MessageProducer producer = session.createProducer(destination);
            if (persistent) {
                producer.setDeliveryMode(2);
            }
            else {
                producer.setDeliveryMode(1);
            }
            if (timeToLive != 0) {
                producer.setTimeToLive((long)timeToLive);
            }
            final ObjectMessage message = session.createObjectMessage((Serializable)event);
            producer.send((Message)message);
        }
        catch (Exception e) {
            throw new MiddlewareEventClientException("Error while firing Middleware Event", (Throwable)e);
        }
        finally {
            try {
                connection.close();
            }
            catch (Throwable t) {}
        }
    }
    
    public void fireAsyncEvent(final MiddlewareEvent event) {
        final boolean persistent = false;
        final int timeToLive = 0;
        Connection connection = null;
        try {
            final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.jmsProviderURL);
            connection = connectionFactory.createConnection();
            connection.start();
            final Session session = connection.createSession(false, 1);
            final Destination destination = (Destination)session.createTopic("GlobalEvent");
            final MessageProducer producer = session.createProducer(destination);
            if (persistent) {
                producer.setDeliveryMode(2);
            }
            else {
                producer.setDeliveryMode(1);
            }
            if (timeToLive != 0) {
                producer.setTimeToLive((long)timeToLive);
            }
            final ObjectMessage message = session.createObjectMessage((Serializable)event);
            producer.send((Message)message);
        }
        catch (Exception e) {
            throw new MiddlewareEventClientException("Error while firing Middleware Event", (Throwable)e);
        }
        finally {
            try {
                connection.close();
            }
            catch (Throwable t) {}
        }
    }
    
    public Response getMWServiceEventOriginators(final Request request) {
        EventOriginData data = null;
        final Set<EventOriginData> l1 = new HashSet<EventOriginData>();
        for (final Map.Entry<String, List<IEventListenerInvokationHandler>> entry : this.eventInvocationMap.entrySet()) {
            final List<IEventListenerInvokationHandler> list = entry.getValue();
            for (final IEventListenerInvokationHandler i : list) {
                data = i.getEventOriginData();
                if (data != null) {
                    l1.add(data);
                }
            }
        }
        final List<EventOriginData> l2 = new ArrayList<EventOriginData>(l1);
        final Response response = new Response();
        final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
        baseValueObjectList.setValueObjectList((List)l2);
        response.put(EventOriginDataParams.ORIGINDATA.name(), (BaseValueObject)baseValueObjectList);
        return response;
    }
    
    public Response getMWServiceEvents(final Request request) {
        EventData data = null;
        final Set<EventData> l1 = new HashSet<EventData>();
        for (final Map.Entry<String, List<IEventListenerInvokationHandler>> entry : this.eventInvocationMap.entrySet()) {
            final List<IEventListenerInvokationHandler> list = entry.getValue();
            for (final IEventListenerInvokationHandler i : list) {
                data = i.getEventData();
                if (data != null) {
                    l1.add(data);
                }
            }
        }
        final List<EventData> l2 = new ArrayList<EventData>(l1);
        final Response response = new Response();
        final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
        baseValueObjectList.setValueObjectList((List)l2);
        response.put("EVENT_DATA", (BaseValueObject)baseValueObjectList);
        return response;
    }
    
    public Response refreshNotificationCache(final Request request) {
        final NotificationRuleData notificationRuleData = (NotificationRuleData)request.get(NotificationRuleParam.NOTIFICATION_RULE.name());
        if (notificationRuleData == null) {
            throw new MiddlewareException("NULL_EVENT_DATA", "null value");
        }
        this.notificationDomainManager.refreshCache(notificationRuleData);
        return this.OK();
    }
}

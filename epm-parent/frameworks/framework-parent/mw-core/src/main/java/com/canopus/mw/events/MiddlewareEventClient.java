package com.canopus.mw.events;

import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.canopus.event.mgr.*;
import org.apache.activemq.*;
import javax.jms.*;
import java.lang.reflect.*;
import java.util.*;
import com.canopus.mw.utils.*;
import com.canopus.mw.*;
import java.io.*;
import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public class MiddlewareEventClient implements IMiddlewareEventClient
{
    @Value("${event.enabled:false}")
    private boolean enabled;
    @Autowired
    private IServiceLocator serviceLocator;
    private String jmsProviderURL;
    private Map<String, EventData> eventDataMap;
    private static Logger log;
    
    public MiddlewareEventClient() {
        this.enabled = false;
        this.jmsProviderURL = "tcp://localhost:61616";
    }
    
    public String getJmsProviderURL() {
        return this.jmsProviderURL;
    }
    
    public void setJmsProviderURL(final String jmsProviderURL) {
        this.jmsProviderURL = jmsProviderURL;
    }
    
    public void initEventData() {
        this.initializeEventVariableData();
    }
    
    private boolean isEnabled() {
        return this.enabled;
    }
    
    private void initializeEventVariableData() {
        if (!this.isEnabled()) {
            return;
        }
        final IEventManager eventManager = (IEventManager)this.serviceLocator.getService("EventManagerServiceImple");
        final Response response = eventManager.getMWServiceEvents(new Request());
        if (null != response) {
            final BaseValueObjectList list = (BaseValueObjectList)response.get("EVENT_DATA");
            if (null != list) {
                final List<EventData> eventData = (List<EventData>)list.getValueObjectList();
                if (null != eventData) {
                    this.eventDataMap = new HashMap<String, EventData>();
                    for (final EventData ed : eventData) {
                        this.eventDataMap.put(ed.getEventId(), ed);
                    }
                }
            }
        }
    }
    
    public void fireEvent(final MiddlewareEvent event) {
        this.fireEvent2(event);
    }
    
    public void fireEventOld(final MiddlewareEvent event) {
        final boolean persistent = false;
        final int timeToLive = 0;
        Connection connection = null;
        try {
            final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.jmsProviderURL);
            connection = connectionFactory.createConnection();
            connection.start();
            final Session session = connection.createSession(false, 1);
            final Destination destination = (Destination)session.createTopic(event.getEventType());
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
            producer.send(message);
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
    
    public void fireEvent1(final MiddlewareEvent event) {
        try {
            final IEventManager eventManager = (IEventManager)this.serviceLocator.getService("EventManagerServiceImple");
            final Request request = new Request();
            request.put("EVENT", (BaseValueObject)event);
            eventManager.fireEvent(request);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Object getFromPayLoad(final String resolver, final MiddlewareEvent event) {
        Object retObj = null;
        if (null == resolver) {
            return null;
        }
        final String[] arr = resolver.split("\\.");
        final Object obj = event.getPayLoadMap().get(arr[0]);
        try {
            final Method method = obj.getClass().getMethod(arr[1], (Class<?>[])new Class[0]);
            retObj = method.invoke(obj, new Object[0]);
        }
        catch (Exception e) {
            MiddlewareEventClient.log.error((Object)("Unable to find value for variable name:" + resolver));
        }
        return retObj;
    }
    
    public Object getFromService(final String resolver, final String resolverServInput, final String resolverServOp, final MiddlewareEvent event) {
        Object param = null;
        Object retObj = null;
        if (null == resolver || null == resolverServOp || null == resolverServInput) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(resolverServInput, ".");
        final String key = tokenizer.nextToken();
        Object obj = event.getPayLoadMap().get(key);
        try {
            final Method method = obj.getClass().getMethod(tokenizer.nextToken(), (Class<?>[])new Class[0]);
            param = method.invoke(obj, new Object[0]);
        }
        catch (Exception e) {
            MiddlewareEventClient.log.error((Object)("Unable to find value for variable name:" + resolverServInput));
        }
        tokenizer = new StringTokenizer(resolverServOp, ".");
        final MiddlewareService service = this.serviceLocator.getService(tokenizer.nextToken());
        final Request request = new Request();
        Response response = null;
        request.put(key, EventClientHelper.getBaseValueObject(param));
        try {
            final Method method2 = service.getClass().getMethod(tokenizer.nextToken(), Request.class);
            response = (Response)method2.invoke(service, request);
        }
        catch (Exception e2) {
            MiddlewareEventClient.log.error((Object)("Unable to find value for method:" + resolverServOp));
        }
        if (null != response) {
            tokenizer = new StringTokenizer(resolver, ".");
            obj = response.get(tokenizer.nextToken());
            try {
                final Method method2 = obj.getClass().getMethod(tokenizer.nextToken(), (Class<?>[])new Class[0]);
                retObj = method2.invoke(obj, new Object[0]);
            }
            catch (Exception e2) {
                MiddlewareEventClient.log.error((Object)("Unable to find value from response:" + resolver));
            }
        }
        return retObj;
    }
    
    public void fireEvent2(final MiddlewareEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        try {
            final IEventManager eventManager = (IEventManager)this.serviceLocator.getService("EventManagerServiceImple");
            final Request request = new Request();
            final MiddlewareEventBean middlewareEventBean = new MiddlewareEventBean();
            middlewareEventBean.setEventType(event.getEventType());
            middlewareEventBean.setIsProcessed(false);
            final ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
            try {
                final ObjectOutputStream out = new ObjectOutputStream(byteArr);
                out.writeObject(event);
                middlewareEventBean.setEventData(byteArr.toByteArray());
            }
            catch (Exception e) {
                throw new DataAccessException("INVALID_SYSTEM_DATA", "Error serializing the payload", (Throwable)e);
            }
            final Map<String, Object> variablesMap = new HashMap<String, Object>();
            final String eventId = event.getEventType();
            if (null == this.eventDataMap) {
                this.initializeEventVariableData();
            }
            if (null != this.eventDataMap) {
                EventData data = this.eventDataMap.get(eventId);
                if (data == null) {
                    this.initializeEventVariableData();
                    data = this.eventDataMap.get(eventId);
                }
                Object obj = null;
                if (null != data) {
                    final List<EventVariableData> variables = (List<EventVariableData>)data.getEventVariables();
                    if (null != variables) {
                        for (final EventVariableData evd : variables) {
                            if ("Input".equalsIgnoreCase(evd.getResolverSourceType())) {
                                obj = this.getFromPayLoad(evd.getResolver(), event);
                            }
                            else if ("Service".equalsIgnoreCase(evd.getResolverSourceType())) {
                                obj = this.getFromService(evd.getResolver(), evd.getResolverServiceInput(), evd.getResolverServiceOperation(), event);
                            }
                            variablesMap.put(evd.getVariableName(), obj);
                        }
                    }
                }
            }
            middlewareEventBean.setVariables((Map)variablesMap);
            request.put("EVENT", (BaseValueObject)middlewareEventBean);
            eventManager.fireEvent(request);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    public void sendToScheduler(final MiddlewareEvent event) {
    }
    
    static {
        MiddlewareEventClient.log = Logger.getLogger(MiddlewareEventClient.class.getName());
    }
}

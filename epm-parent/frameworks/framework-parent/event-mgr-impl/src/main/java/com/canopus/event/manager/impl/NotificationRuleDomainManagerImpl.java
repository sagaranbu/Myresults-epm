package com.canopus.event.manager.impl;

import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import com.canopus.event.manager.camelroutes.*;
import com.canopus.event.mgr.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.*;
import org.apache.camel.impl.*;
import org.apache.camel.builder.*;
import org.apache.camel.*;
import org.apache.camel.component.properties.*;
import java.util.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.dto.*;
import javax.annotation.*;

@Component
public class NotificationRuleDomainManagerImpl
{
    @Autowired
    EventDataService eventDataService;
    @Autowired
    NotificationRuleDataService notificationRuleDataService;
    @Autowired
    NotificationRouteBuilder notificationRouteBuilder;
    @Value("#{eventInvocationMap}")
    private Map<String, List<IEventListenerInvokationHandler>> eventInvocationMap;
    private CamelContext camelContext;
    @Autowired
    DashboardMessageDataService dashboardMessageDataService;
    private Map<Integer, EventData> eventMap;
    
    public void refreshCache(final NotificationRuleData notificationRule) {
        ExecutionContext.getCurrent().setCrossTenant();
        final List<EventData> events = this.getAllEvents();
        ExecutionContext.getCurrent().unSetCrossTenant();
        this.eventMap = new HashMap<Integer, EventData>();
        for (final EventData event : events) {
            this.eventMap.put(event.getId(), event);
        }
        final EventData event2 = this.eventMap.get(notificationRule.getEventId());
        boolean found = false;
        List<IEventListenerInvokationHandler> eventListenerInvokationHandlers = this.eventInvocationMap.get(event2.getEventId());
        if (eventListenerInvokationHandlers == null) {
            eventListenerInvokationHandlers = new ArrayList<IEventListenerInvokationHandler>();
            this.eventInvocationMap.put(event2.getEventId(), eventListenerInvokationHandlers);
        }
        for (final IEventListenerInvokationHandler handler : eventListenerInvokationHandlers) {
            if (handler instanceof NotificationEventListnerInvocationHandler) {
                final NotificationEventListnerInvocationHandler notHandler = (NotificationEventListnerInvocationHandler)handler;
                if (notHandler.getNotificationRuleData().getId().equals(notificationRule.getId())) {
                    notHandler.setNotificationRuleData(notificationRule);
                    notHandler.setInvokeMechanism(notificationRule.getEventHandlingMechanism());
                    found = true;
                    break;
                }
                continue;
            }
        }
        if (!found) {
            final IEventListenerInvokationHandler handler2 = (IEventListenerInvokationHandler)new NotificationEventListnerInvocationHandler(this.camelContext, event2, notificationRule, "notification", notificationRule.getEventHandlingMechanism());
            eventListenerInvokationHandlers.add(handler2);
        }
    }
    
    @PostConstruct
    public void init() {
        ExecutionContext.getCurrent().setCrossTenant();
        final List<EventData> events = this.getAllEvents();
        final List<NotificationRuleData> notificationRules = this.getAllNotificationRules();
        ExecutionContext.getCurrent().unSetCrossTenant();
        this.eventMap = new HashMap<Integer, EventData>();
        for (final EventData event : events) {
            this.eventMap.put(event.getId(), event);
        }
        IEventListenerInvokationHandler handler = null;
        try {
            this.camelContext = (CamelContext)new DefaultCamelContext();
            final List<RouteBuilder> routes = this.notificationRouteBuilder.getRoutes();
            for (final RouteBuilder route : routes) {
                this.camelContext.addRoutes((RoutesBuilder)route);
            }
            final PropertiesComponent pc = new PropertiesComponent();
            pc.setLocation("classpath:eventmanager.properties");
            this.camelContext.addComponent("properties", (org.apache.camel.Component)pc);
            for (final NotificationRuleData notificationRule : notificationRules) {
                final EventData event2 = this.eventMap.get(notificationRule.getEventId());
                if (event2 == null) {
                    continue;
                }
                handler = (IEventListenerInvokationHandler)new NotificationEventListnerInvocationHandler(this.camelContext, event2, notificationRule, "notification", notificationRule.getEventHandlingMechanism());
                List<IEventListenerInvokationHandler> eventListenerInvokationHandlers = this.eventInvocationMap.get(event2.getEventId());
                if (eventListenerInvokationHandlers == null) {
                    eventListenerInvokationHandlers = new ArrayList<IEventListenerInvokationHandler>();
                    this.eventInvocationMap.put(event2.getEventId(), eventListenerInvokationHandlers);
                }
                eventListenerInvokationHandlers.add(handler);
            }
            this.camelContext.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private List<NotificationRuleData> getAllNotificationRules() {
        final Response response = this.notificationRuleDataService.getAllNotificationRules(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(NotificationRuleParam.NOTIFICATIONRULES.name());
        if (list != null) {
            final List<NotificationRuleData> actualList = (List<NotificationRuleData>)list.getValueObjectList();
            return actualList;
        }
        return null;
    }
    
    public List<EventData> getAllEvents() {
        final Response response = this.eventDataService.getAllEvents(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EventParam.EVENTLIST.name());
        if (list != null) {
            final List<EventData> actualList = (List<EventData>)list.getValueObjectList();
            return actualList;
        }
        return null;
    }
    
    public Integer saveDashboardMessage(final String message, final Map<String, Object> headers) {
        final Request request = new Request();
        final String subject = (String) headers.get("Subject");
        final Integer notificationId = (Integer) headers.get("notificationId");
        final DashboardMessageData dashboardMessageData = new DashboardMessageData();
        dashboardMessageData.setNotificationId(notificationId);
        dashboardMessageData.setMessage(message);
        dashboardMessageData.setSubject(subject);
        dashboardMessageData.setMessageTime(new Date());
        dashboardMessageData.setStatus("Active");
        dashboardMessageData.setCancellable(true);
        final List<Integer> recipientUserIds = (List<Integer>) headers.get("To");
        dashboardMessageData.setRecipientUserIds((List)recipientUserIds);
        request.put(DashboardMessageParam.DASHBOARD_MESSAGE.name(), (BaseValueObject)dashboardMessageData);
        final Response response = this.dashboardMessageDataService.saveDashboardMessage(request);
        final Identifier identifier = (Identifier)response.get(DashboardMessageParam.DASHBOARDMESSAGE_ID.name());
        return identifier.getId();
    }
    
    public Response getDashboardMessages(final Request request) {
        final Response response = this.dashboardMessageDataService.getDashboardMessages(request);
        return response;
    }
    
    public Response cancelUserDashboardMessage(final Request request) {
        final Response response = this.dashboardMessageDataService.cancelUserDashboardMessage(request);
        return response;
    }
    
    @PreDestroy
    public void destroy() {
        try {
            Thread.sleep(20000L);
            this.camelContext.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

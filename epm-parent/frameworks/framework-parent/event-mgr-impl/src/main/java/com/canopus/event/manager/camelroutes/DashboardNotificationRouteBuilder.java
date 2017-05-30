package com.canopus.event.manager.camelroutes;

import org.apache.camel.builder.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import org.apache.log4j.*;
import com.canopus.event.manager.impl.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.camel.model.*;
import org.apache.camel.*;
import com.canopus.mw.dto.*;
import java.util.*;
import freemarker.cache.*;
import java.io.*;
import freemarker.template.*;

@Component
public class DashboardNotificationRouteBuilder extends RouteBuilder
{
    private static Configuration configuration;
    private static Logger logger;
    @Autowired
    DashboardMessageUtils dashboardMessageUtils;
    
    public void configure() throws Exception {
        try {
            final RouteDefinition routeDefinition = (RouteDefinition)((RouteDefinition)((RouteDefinition)((RouteDefinition)((RouteDefinition)this.from("direct:dashboard").log("Dashboard Notification called...")).process((Processor)new Processor() {
                public void process(final Exchange exchange) throws Exception {
                    final Map<String, Object> inHeaders = (Map<String, Object>)exchange.getIn().getHeaders();
                    final NotificationRuleData notificationRule = (NotificationRuleData) inHeaders.get("notificationRule");
                    inHeaders.put("notificationId", notificationRule.getId());
                    final List<NotificationRecipientData> recipients = (List<NotificationRecipientData>)notificationRule.getNotificationRecipients();
                    final List<Integer> tos = new ArrayList<Integer>();
                    Map<String, Object> variables = new HashMap<String, Object>();
                    final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)exchange.getIn().getHeader("middlewareEventBean");
                    variables = (Map<String, Object>)middlewareEventBean.getVariables();
                    for (final NotificationRecipientData recipient : recipients) {
                        if (recipient.getRecipientType().equals("Manual")) {
                            tos.add(Integer.parseInt(recipient.getRecipient()));
                        }
                        else {
                            if (!recipient.getRecipientType().equals("variables")) {
                                continue;
                            }
                            tos.add((Integer) variables.get(recipient.getRecipient()));
                        }
                    }
                    inHeaders.put("To", tos);
                }
            })).process((Processor)new Processor() {
                public void process(final Exchange exchange) throws Exception {
                    final Map<String, Object> inHeaders = (Map<String, Object>)exchange.getIn().getHeaders();
                    final String subjectTemplateString = (String) inHeaders.get("Subject");
                    final StringTemplateLoader stringLoader = new StringTemplateLoader();
                    stringLoader.putTemplate("subjectTemplate", subjectTemplateString);
                    DashboardNotificationRouteBuilder.configuration.setTemplateLoader((TemplateLoader)stringLoader);
                    final Template subjectTemplate = DashboardNotificationRouteBuilder.configuration.getTemplate("subjectTemplate");
                    Map<String, Object> variables = new HashMap<String, Object>();
                    final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)exchange.getIn().getHeader("middlewareEventBean");
                    variables = (Map<String, Object>)middlewareEventBean.getVariables();
                    final StringWriter out = new StringWriter();
                    subjectTemplate.process((Object)variables, (Writer)out);
                    inHeaders.put("Subject", out.toString());
                }
            })).process((Processor)new Processor() {
                public void process(final Exchange exchange) throws Exception {
                    final String bodyAsString = (String)exchange.getIn().getBody();
                    final StringTemplateLoader stringLoader = new StringTemplateLoader();
                    stringLoader.putTemplate("bodyTemplate", bodyAsString);
                    DashboardNotificationRouteBuilder.configuration.setTemplateLoader((TemplateLoader)stringLoader);
                    final Template bodyTemplate = DashboardNotificationRouteBuilder.configuration.getTemplate("bodyTemplate");
                    Map<String, Object> variables = new HashMap<String, Object>();
                    final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)exchange.getIn().getHeader("middlewareEventBean");
                    variables = (Map<String, Object>)middlewareEventBean.getVariables();
                    final StringWriter out = new StringWriter();
                    bodyTemplate.process((Object)variables, (Writer)out);
                    exchange.getIn().setBody((Object)out.toString());
                }
            })).bean((Object)this.dashboardMessageUtils, "saveDashboardMessage(${body}, ${headers})");
        }
        catch (Exception e) {
            DashboardNotificationRouteBuilder.logger.error((Object)"Error occured while processing dashboard apache camel route", (Throwable)e);
        }
    }
    
    static {
        DashboardNotificationRouteBuilder.configuration = new Configuration();
        DashboardNotificationRouteBuilder.logger = Logger.getLogger((Class)DashboardNotificationRouteBuilder.class);
    }
}

package com.canopus.event.manager.camelroutes;

import org.apache.camel.builder.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.camel.model.*;
import org.apache.camel.*;
import com.canopus.mw.dto.*;
import java.util.*;
import freemarker.cache.*;
import java.io.*;
import freemarker.template.*;

@Component
public class EmailNotificationRouteBuilder extends RouteBuilder
{
    private static Configuration configuration;
    private static Logger logger;
    @Value("${notification.type:smtp}")
    private String notificationType;
    
    public EmailNotificationRouteBuilder() {
        this.notificationType = "smtp";
    }
    
    public void configure() throws Exception {
        try {
            final RouteDefinition routeDefinition = (RouteDefinition)((RouteDefinition)((RouteDefinition)((RouteDefinition)this.from("direct:email").process((Processor)new Processor() {
                public void process(final Exchange exchange) throws Exception {
                    final Map<String, Object> inHeaders = (Map<String, Object>)exchange.getIn().getHeaders();
                    final NotificationRuleData notificationRule = (NotificationRuleData) inHeaders.get("notificationRule");
                    final List<NotificationRecipientData> recipients = (List<NotificationRecipientData>)notificationRule.getNotificationRecipients();
                    final List<String> tos = new ArrayList<String>();
                    Map<String, Object> variables = new HashMap<String, Object>();
                    final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)exchange.getIn().getHeader("middlewareEventBean");
                    variables = (Map<String, Object>)middlewareEventBean.getVariables();
                    for (final NotificationRecipientData recipient : recipients) {
                        if (recipient.getRecipientType().equals("Manual")) {
                            tos.add(recipient.getRecipient());
                        }
                        else {
                            if (!recipient.getRecipientType().equals("variables")) {
                                continue;
                            }
                            tos.add((String) variables.get(recipient.getRecipient()));
                        }
                    }
                    inHeaders.put("To", tos);
                }
            })).process((Processor)new Processor() {
                public void process(final Exchange exchange) throws Exception {
                    final Map<String, Object> inHeaders = (Map<String, Object>)exchange.getIn().getHeaders();
                    final String subjectTemplateString = (String) inHeaders.get("Subject");
                    final StringTemplateLoader stringLoader = new StringTemplateLoader();
                    stringLoader.putTemplate("emailSubjectTemplate", subjectTemplateString);
                    EmailNotificationRouteBuilder.configuration.setTemplateLoader((TemplateLoader)stringLoader);
                    final Template subjectTemplate = EmailNotificationRouteBuilder.configuration.getTemplate("emailSubjectTemplate");
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
                    stringLoader.putTemplate("emailBodyTemplate", bodyAsString);
                    EmailNotificationRouteBuilder.configuration.setTemplateLoader((TemplateLoader)stringLoader);
                    final Template bodyTemplate = EmailNotificationRouteBuilder.configuration.getTemplate("emailBodyTemplate");
                    Map<String, Object> variables = new HashMap<String, Object>();
                    final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)exchange.getIn().getHeader("middlewareEventBean");
                    variables = (Map<String, Object>)middlewareEventBean.getVariables();
                    final StringWriter out = new StringWriter();
                    bodyTemplate.process((Object)variables, (Writer)out);
                    exchange.getIn().setBody((Object)out.toString());
                }
            })).to(this.notificationType + "://{{notification.server}}?username={{notification.email}}&password={{notification.password}}&contentType={{notification.contenttype}}");
        }
        catch (Exception e) {
            EmailNotificationRouteBuilder.logger.error((Object)"Unable to send email.", (Throwable)e);
        }
    }
    
    static {
        EmailNotificationRouteBuilder.configuration = new Configuration();
        EmailNotificationRouteBuilder.logger = Logger.getLogger((Class)EmailNotificationRouteBuilder.class);
    }
}

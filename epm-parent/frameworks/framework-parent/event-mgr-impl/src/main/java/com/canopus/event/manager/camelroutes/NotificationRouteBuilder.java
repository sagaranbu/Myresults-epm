package com.canopus.event.manager.camelroutes;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.camel.builder.*;
import java.util.*;

@Component
public class NotificationRouteBuilder
{
    @Autowired
    EmailNotificationRouteBuilder emailNotificationRouteBuilder;
    @Autowired
    DashboardNotificationRouteBuilder dashboardNotificationRouteBuilder;
    
    public List<RouteBuilder> getRoutes() {
        final List<RouteBuilder> routes = new ArrayList<RouteBuilder>();
        routes.add(this.emailNotificationRouteBuilder);
        routes.add(this.dashboardNotificationRouteBuilder);
        return routes;
    }
}

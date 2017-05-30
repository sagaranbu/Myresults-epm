package com.canopus.event.manager.impl;

import org.springframework.stereotype.*;
import org.springframework.context.*;
import java.util.*;
import org.springframework.beans.*;

@Component
public class DashboardMessageUtils implements ApplicationContextAware
{
    private ApplicationContext applicationContext;
    
    public Integer saveDashboardMessage(final String message, final Map<String, Object> headers) {
        final NotificationRuleDomainManagerImpl notificationRuleDomainManager = (NotificationRuleDomainManagerImpl)this.applicationContext.getBean((Class)NotificationRuleDomainManagerImpl.class);
        return notificationRuleDomainManager.saveDashboardMessage(message, headers);
    }
    
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

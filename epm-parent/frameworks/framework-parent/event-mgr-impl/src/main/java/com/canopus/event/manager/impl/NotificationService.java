package com.canopus.event.manager.impl;

import com.canopus.mw.*;
import com.canopus.event.mgr.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ INotificationService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class NotificationService extends BaseMiddlewareBean implements INotificationService
{
    @Autowired
    NotificationRuleDomainManagerImpl notificationRuleDomainManager;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getDashboardMessages(final Request request) {
        final Response response = this.notificationRuleDomainManager.getDashboardMessages(request);
        return response;
    }
    
    public Response cancelUserDashboardMessage(final Request request) {
        final Response response = this.notificationRuleDomainManager.cancelUserDashboardMessage(request);
        return response;
    }
}

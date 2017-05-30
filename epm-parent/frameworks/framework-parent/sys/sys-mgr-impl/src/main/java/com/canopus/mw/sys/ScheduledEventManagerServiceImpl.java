package com.canopus.mw.sys;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.mw.sys.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.codehaus.jackson.map.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.MiddlewareEventBean;
import com.canopus.entity.vo.*;
import com.canopus.mw.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ScheduledEventManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class ScheduledEventManagerServiceImpl extends BaseMiddlewareBean implements ScheduledEventManagerService
{
    @Autowired
    private ScheduledEventDomainManager scheduledEventDomainManager;
    ObjectMapper mapper;
    
    public ScheduledEventManagerServiceImpl() {
        this.mapper = new ObjectMapper();
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response scheduleEvent(final Request request) {
        final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)request.get(ScheduledEventParams.MIDDLEWARE_EVENT.name());
        if (middlewareEventBean == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_SYSTEM_DATA", "No input data in the request"));
        }
        this.scheduledEventDomainManager.addScheduledEvent(middlewareEventBean);
        return this.OK();
    }
}

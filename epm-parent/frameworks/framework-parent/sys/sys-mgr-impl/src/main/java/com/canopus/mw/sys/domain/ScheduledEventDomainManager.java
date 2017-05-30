package com.canopus.mw.sys.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.MiddlewareEventBean;

@Component
public class ScheduledEventDomainManager extends BaseDomainManager
{
    @Autowired
    private MiddlewareEventDataService dataService;
    
    public Integer addScheduledEvent(final MiddlewareEventBean middlewareEventBean) {
        final Request request = new Request();
        request.put(ScheduledEventDacParams.MIDDLEWARE_EVENT.getParamName(), (BaseValueObject)middlewareEventBean);
        final Response response = this.dataService.addScheduledEvent(request);
        final Identifier id = (Identifier)response.get(ScheduledEventDacParams.MIDDLEWARE_EVENT_ID.name());
        return id.getId();
    }
    
    public List<MiddlewareEventBean> getEventsToFire() {
        final Request request = new Request();
        final Response response = this.dataService.getEventsToFire(request);
        final BaseValueObjectList eventsToFire = (BaseValueObjectList)response.get(ScheduledEventDacParams.EVENTS_TO_FIRE.name());
        return (List<MiddlewareEventBean>)eventsToFire.getValueObjectList();
    }
    
    public void setProcessed(final Integer id, final boolean isProcessed) {
        final Request request = new Request();
        request.put(ScheduledEventDacParams.MIDDLEWARE_EVENT_ID.getParamName(), (BaseValueObject)new Identifier(id));
        request.put(ScheduledEventDacParams.MIDDLEWARE_EVENT_PROCESSED.getParamName(), (BaseValueObject)new BooleanResponse(isProcessed));
        final Response response = this.dataService.setProcessed(request);
    }
}

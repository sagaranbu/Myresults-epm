package com.canopus.entity.impl;

import org.springframework.stereotype.*;
import com.canopus.entity.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.vo.MiddlewareEventBean;
import com.canopus.entity.domain.*;
import com.canopus.entity.*;
import com.canopus.mw.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;
import java.util.*;
import java.lang.reflect.*;
import com.canopus.mw.dto.*;
import java.io.*;

@Service
public class MiddlewareEventDataServiceImpl extends BaseDataAccessService implements MiddlewareEventDataService
{
    @Autowired
    private MiddlewareEventDao middlewareEventDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public MiddlewareEventDataServiceImpl() {
        this.genericDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)MiddlewareEventBean.class, (Class)MiddlewareEventData.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response addScheduledEvent(final Request request) {
        final MiddlewareEventBean middlewareEventBean = (MiddlewareEventBean)request.get(ScheduledEventDacParams.MIDDLEWARE_EVENT.getParamName());
        if (middlewareEventBean == null || (middlewareEventBean.getId() != null && middlewareEventBean.getId() > 0)) {
            return this.ERROR((Exception)new MiddlewareException("MIDDLEWARE-EVENT-100", "No input data in the request or not a new event"));
        }
        middlewareEventBean.setId(0);
        final MiddlewareEventData data = new MiddlewareEventData();
        try {
            this.modelMapper.map((Object)middlewareEventBean, (Object)data);
            this.middlewareEventDao.save(data);
            return this.OK(ScheduledEventDacParams.MIDDLEWARE_EVENT_ID.name(), (BaseValueObject)new Identifier(data.getId()));
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            return this.ERROR((Exception)new DataAccessException("MIDDLEWARE-EVENT-000", "Unknown error while loading system", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEventsToFire(final Request request) {
        final ExampleOptions options = new ExampleOptions();
        options.setLikeMode(3);
        final Filter filter = Filter.and(new Filter[] { Filter.equal("isProcessed", (Object)new Boolean(false)), Filter.lessOrEqual("scheduleDate", (Object)new Date()) });
        final Search search = new Search((Class)MiddlewareEventData.class);
        search.addFilter(filter);
        final List<MiddlewareEventData> result = (List<MiddlewareEventData>)this.middlewareEventDao.search((ISearch)search);
        final Type listType = new TypeToken<List<MiddlewareEventBean>>() {}.getType();
        final List<MiddlewareEventBean> eventsToFire = (List<MiddlewareEventBean>)this.modelMapper.map((Object)result, listType);
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)eventsToFire);
        return this.OK(ScheduledEventDacParams.EVENTS_TO_FIRE.getParamName(), (BaseValueObject)list);
    }
    
    @Transactional
    public Response setProcessed(final Request request) {
        final Identifier id = (Identifier)request.get(ScheduledEventDacParams.MIDDLEWARE_EVENT_ID.name());
        final BooleanResponse isProcessed = (BooleanResponse)request.get(ScheduledEventDacParams.MIDDLEWARE_EVENT_PROCESSED.name());
        if (id == null || id.getId() == null) {
            return this.ERROR((Exception)new DataAccessException("", "Invalid data in request"));
        }
        MiddlewareEventData event = null;
        event = (MiddlewareEventData)this.middlewareEventDao.find((Serializable)id.getId());
        try {
            event.setIsProcessed(isProcessed == null || isProcessed.isResponse());
            this.middlewareEventDao.save(event);
            return this.OK();
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException("", "Unknown error while loading employee", (Throwable)ex));
        }
    }
}

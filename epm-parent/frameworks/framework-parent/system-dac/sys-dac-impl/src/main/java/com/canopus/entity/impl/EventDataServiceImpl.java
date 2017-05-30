package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.*;
import com.canopus.entity.domain.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.param.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;

@Component
public class EventDataServiceImpl extends BaseDataAccessService implements EventDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public EventDataServiceImpl() {
        this.genericDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)EventVariableData.class, (Class)EventVariable.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EventData.class, (Class)Event.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveEventVariable(final Request request) {
        final EventVariableData variable = (EventVariableData)request.get("EVENT_VARIABLE");
        if (variable == null) {
            return this.ERROR((Exception)new DataAccessException("evnt-var-err-000", "Invalid data in request"));
        }
        EventVariable variableEntity;
        if (variable.getId() != null) {
            variableEntity = (EventVariable)this.genericDao.find((Class)EventVariable.class, (Serializable)variable.getId());
        }
        else {
            variableEntity = new EventVariable();
        }
        this.modelMapper.map((Object)variable, (Object)variableEntity);
        this.genericDao.save((Object)variableEntity);
        variable.setId(variableEntity.getId());
        return null;
    }
    
    @Transactional
    public Response saveEvent(final Request request) {
        final EventData eventData = (EventData)request.get(EventParam.EVENTDATA.name());
        if (eventData == null) {
            return this.ERROR((Exception)new DataAccessException("evnt-err-002", "Unknown error while getting Events."));
        }
        Event event;
        if (eventData.getId() != null) {
            event = (Event)this.genericDao.find((Class)Event.class, (Serializable)eventData.getId());
        }
        else {
            event = new Event();
        }
        this.modelMapper.map((Object)eventData, (Object)event);
        this.genericDao.save((Object)event);
        eventData.setId(event.getId());
        return this.OK(EventParam.EVENTID.name(), (BaseValueObject)new Identifier(eventData.getId()));
    }
    
    @Transactional(readOnly = true)
    public Response getAllEvents(final Request request) {
        try {
            final List<Event> events = (List<Event>)this.genericDao.findAll((Class)Event.class);
            final Type listType = new TypeToken<List<EventData>>() {}.getType();
            final List<EventData> eventDataList = (List<EventData>)this.modelMapper.map((Object)events, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)eventDataList);
            return this.OK(EventParam.EVENTLIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException("evnt-err-001", "Unknown error while getting Events.", (Throwable)e));
        }
    }
}

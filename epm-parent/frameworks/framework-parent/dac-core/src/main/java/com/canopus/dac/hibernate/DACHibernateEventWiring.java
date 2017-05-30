package com.canopus.dac.hibernate;

import org.springframework.stereotype.*;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.hibernate.event.service.spi.*;
import org.hibernate.engine.spi.*;
import org.hibernate.event.spi.*;
import javax.annotation.*;

@Component
public class DACHibernateEventWiring
{
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private DACHibernateEventListener listener;
    
    @PostConstruct
    public void registerListeners() {
        final EventListenerRegistry registry = (EventListenerRegistry)((SessionFactoryImplementor)this.sessionFactory).getServiceRegistry().getService((Class)EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).prependListener(this.listener);
        registry.getEventListenerGroup(EventType.PRE_INSERT).prependListener(this.listener);
    }
}

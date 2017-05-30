package com.canopus.dac.hibernate;

import org.springframework.stereotype.*;
import com.canopus.dac.utils.*;
import org.hibernate.event.spi.*;

@Component
public class DACHibernateEventListener implements PreInsertEventListener, PreUpdateEventListener
{
    public boolean onPreInsert(final PreInsertEvent event) {
        HibernateDAOHelper.setSystemData(event.getEntity());
        return false;
    }
    
    public boolean onPreUpdate(final PreUpdateEvent event) {
        HibernateDAOHelper.setSystemData(event.getEntity());
        return false;
    }
}

package com.canopus.mw.events;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import javax.jms.*;

public abstract class BaseMiddlewareEventListener extends BaseMiddlewareBean implements MessageListener
{
    public void onMessage(final Message message) {
        try {
            final Object o = ((ObjectMessage)message).getObject();
            if (o instanceof MiddlewareEvent) {
                this.handleEvent((MiddlewareEvent)o);
            }
            else if (o instanceof MiddlewareEventBean) {
                this.handleEvent((MiddlewareEventBean)o);
            }
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public abstract void handleEvent(final MiddlewareEvent p0);
    
    public abstract void handleEvent(final MiddlewareEventBean p0);
}

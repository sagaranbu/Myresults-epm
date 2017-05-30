package com.canopus.mw.events;

import javax.jms.*;

public abstract class BaseMiddlewareScheduleEventListener extends BaseMiddlewareEventListener
{
    @Override
    public void onMessage(final Message message) {
    }
}

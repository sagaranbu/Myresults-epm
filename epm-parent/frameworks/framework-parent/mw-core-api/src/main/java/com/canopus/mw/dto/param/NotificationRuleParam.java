package com.canopus.mw.dto.param;

public enum NotificationRuleParam implements IMiddlewareParam
{
    NOTIFICATION_RULE, 
    NOTIFICATION_RULEID, 
    NOTIFICATIONRULES;
    
    @Override
    public String getParamName() {
        return this.name();
    }
}

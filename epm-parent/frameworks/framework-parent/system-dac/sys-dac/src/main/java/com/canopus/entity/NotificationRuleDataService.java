package com.canopus.entity;

import com.canopus.mw.dto.*;

public interface NotificationRuleDataService
{
    public static final String ERR_NOTIFICATION_RULE_INVALID_DATA = "ntf-err-000";
    public static final String ERR_NOTIFICATION_RULE_UNKNOWN_EXCEPTION = "ntf-err-001";
    
    Response saveNotificationRule(final Request p0);
    
    Response getAllNotificationRules(final Request p0);
}

package com.canopus.mw.dto.param;

public enum DashboardMessageParam implements IMiddlewareParam
{
    DASHBOARDMESSAGE_ID, 
    DASHBOARD_MESSAGE, 
    USER_ID, 
    DASHBOARD_MSGS, 
    DASHBOARD_MSGUSR, 
    DASHBOARD_USR_MSGS, 
    DASHBOARD_MESSAGE_IDS;
    
    @Override
    public String getParamName() {
        return this.name();
    }
}

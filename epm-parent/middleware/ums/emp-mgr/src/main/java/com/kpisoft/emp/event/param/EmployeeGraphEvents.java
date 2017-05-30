package com.kpisoft.emp.event.param;

import com.canopus.mw.events.*;

public enum EmployeeGraphEvents implements IMiddlewareEventType
{
    SUSPEND_SUP_REL_EVENT, 
    SUSPEND_EMP_POS_REL_EVENT, 
    SUSPEND_EMP_ORG_REL_EVENT, 
    CREATE_EMPLOYEE_EVENT;
    
    public String getEventId() {
        return this.getClass().getName() + "." + this.name();
    }
}

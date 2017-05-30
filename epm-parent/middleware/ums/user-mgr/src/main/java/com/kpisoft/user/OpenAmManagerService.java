package com.kpisoft.user;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.openam.*;

public interface OpenAmManagerService extends MiddlewareService
{
    public static final String ERR_AUTH_UNKNOWN_EXCEPTION = "AUTH-000";
    
    Session authenticateUser(final UserLoginData p0);
}

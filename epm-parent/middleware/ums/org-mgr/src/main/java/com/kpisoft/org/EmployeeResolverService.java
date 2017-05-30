package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface EmployeeResolverService extends MiddlewareService
{
    public static final String ERR_INVALID_INPUT = "Invalid_Input";
    public static final String ERR_UNKNOWN_EXCEPTION = "RESOLVER-001";
    public static final String ERR_UNRESOLVED_EXCEPTION = "RESOLVER-101";
    
    Response getEmployeeBasedOnExpression(final Request p0);
}

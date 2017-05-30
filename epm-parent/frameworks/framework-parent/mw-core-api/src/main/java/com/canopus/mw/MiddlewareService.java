package com.canopus.mw;

import com.canopus.mw.dto.*;

public interface MiddlewareService
{
    public static final String ERR_NO_TENANT_TO_CXT = "CXT-NO-TENANT-00";
    
    StringIdentifier getServiceId();
}

package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface OrganizationInterceptorService extends MiddlewareService
{
    Response beforeCreate(final Request p0);
    
    Response beforeUpdate(final Request p0);
    
    Response beforeDelete(final Request p0);
    
    Response beforeSuspend(final Request p0);
}

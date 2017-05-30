package com.canopus.saas;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface TenantBaseManagerService extends MiddlewareService
{
    Response createTenant(final Request p0);
    
    Response getTenant(final Request p0);
    
    Response updateTenant(final Request p0);
    
    Response removeTenant(final Request p0);
    
    Response getAllTenants(final Request p0);
}

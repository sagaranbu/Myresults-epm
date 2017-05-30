package com.canopus.saas.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface TenantBaseDataService extends DataAccessService
{
    public static final String ERR_TENANT_UNKNOWN_EXCEPTION = "TARGET-DAC-000";
    public static final String ERR_TENANT_DOES_NOT_EXIST = "TARGET-DAC-001";
    
    Response createTenant(final Request p0);
    
    Response removeTenant(final Request p0);
    
    Response getTenant(final Request p0);
    
    Response getAllTeanats(final Request p0);
}

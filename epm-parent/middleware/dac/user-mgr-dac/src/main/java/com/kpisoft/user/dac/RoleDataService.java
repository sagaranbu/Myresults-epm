package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface RoleDataService extends DataAccessService
{
    Response getRole(final Request p0);
    
    Response saveRole(final Request p0);
    
    Response deleteRole(final Request p0);
    
    Response getAllRoles(final Request p0);
    
    Response searchRole(final Request p0);
    
    Response getRolesCount(final Request p0);
}

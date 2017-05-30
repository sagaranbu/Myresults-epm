package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface RoleManagerService extends MiddlewareService
{
    Response getRole(final Request p0);
    
    Response createRole(final Request p0);
    
    Response updateRole(final Request p0);
    
    Response deleteRole(final Request p0);
    
    Response getAllRoles(final Request p0);
    
    Response searchRole(final Request p0);
    
    Response getRolesCount(final Request p0);
}

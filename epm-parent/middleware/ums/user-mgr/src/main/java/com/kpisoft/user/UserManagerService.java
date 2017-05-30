package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface UserManagerService extends MiddlewareService
{
    Response getUser(final Request p0);
    
    Response getUserByLogin(final Request p0);
    
    Response getUserByExtCode(final Request p0);
    
    Response createUser(final Request p0);
    
    Response updateUser(final Request p0);
    
    Response deleteUser(final Request p0);
    
    Response getUserAuthorization(final Request p0);
    
    Response search(final Request p0);
    
    Response deleteUsersByEmployeeIds(final Request p0);
    
    Response suspendUsersByEmployeeIds(final Request p0);
    
    Response getEmployeeIdsByRoles(final Request p0);
    
    Response getUserMapByRoles(final Request p0);
    
    Response getUserByEmployeeId(final Request p0);
    
    @Deprecated
    Response getAllUsersByRoleId(final Request p0);
    
    Response getUsersIdEmailFromRoleList(final Request p0);
}

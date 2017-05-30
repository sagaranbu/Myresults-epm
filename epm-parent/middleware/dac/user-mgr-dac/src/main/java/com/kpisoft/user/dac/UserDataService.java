package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface UserDataService extends DataAccessService
{
    Response getUser(final Request p0);
    
    Response getUserByLogin(final Request p0);
    
    Response getUserByExtCode(final Request p0);
    
    Response saveUser(final Request p0);
    
    Response deleteUser(final Request p0);
    
    Response search(final Request p0);
    
    Response deleteUsersByEmployeeIds(final Request p0);
    
    Response suspendUsersByEmployeeIds(final Request p0);
    
    Response getEmployeeIdsByRoles(final Request p0);
    
    Response getUserMapByRoles(final Request p0);
    
    Response getUserByEmployeeId(final Request p0);
    
    Response getUsersIdEmailFromRoleList(final Request p0);
    
    Response loadFrequentUsersToCache(final Request p0);
    
    Response loadUsersToCache(final Request p0);
    
    Response getActiveUserCount(final Request p0);
}

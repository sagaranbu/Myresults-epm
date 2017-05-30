package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface UserGroupManagerService extends MiddlewareService
{
    Response updateUserGroup(final Request p0);
    
    Response createUserGroup(final Request p0);
    
    Response getUserGroup(final Request p0);
    
    Response deleteUserGroup(final Request p0);
    
    Response getAllGroups(final Request p0);
    
    Response search(final Request p0);
}

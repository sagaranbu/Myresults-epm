package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface UserGroupDataService extends DataAccessService
{
    Response saveUserGroup(final Request p0);
    
    Response getUserGroup(final Request p0);
    
    Response getAllGroups(final Request p0);
    
    Response deleteUserGroup(final Request p0);
    
    Response search(final Request p0);
}

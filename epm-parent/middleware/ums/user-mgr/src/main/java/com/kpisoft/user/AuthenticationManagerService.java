package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface AuthenticationManagerService extends MiddlewareService
{
    Response loginUser(final Request p0);
    
    Response logoutUser(final Request p0);
    
    Response validateSession(final Request p0);
    
    Response getUserProfile(final Request p0);
    
    Response createUser(final Request p0);
    
    Response updateUser(final Request p0);
    
    Response deleteUser(final Request p0);
    
    Response getRedirectUrl(final Request p0);
}

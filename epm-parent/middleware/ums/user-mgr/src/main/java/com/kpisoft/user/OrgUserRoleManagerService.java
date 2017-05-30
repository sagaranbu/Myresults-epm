package com.kpisoft.user;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface OrgUserRoleManagerService extends MiddlewareService
{
    public static final String ERR_INVALID_INPUT = "ORG-USER-ROLE-001";
    public static final String ERR_RECORD_DOES_NOT_EXIST = "ORG-USER-ROLE-002";
    public static final String ERR_UNKNOWN_EXCEPTION = "ORG-USER-ROLE-003";
    
    Response saveOrgUserRole(final Request p0);
    
    Response updateOrgUserRole(final Request p0);
    
    Response getOrgUserRole(final Request p0);
    
    Response search(final Request p0);
    
    Response removeOrgUserRole(final Request p0);
    
    Response removeRelationForUserIds(final Request p0);
    
    Response removeRelationForRoleIds(final Request p0);
    
    Response removeRelationForOrgIds(final Request p0);
}

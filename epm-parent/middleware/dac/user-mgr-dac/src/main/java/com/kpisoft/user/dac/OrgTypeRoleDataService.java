package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OrgTypeRoleDataService extends DataAccessService
{
    public static final String ERR_INVALID_INPUT = "ORGTYPE-ROLE-DAC-001";
    public static final String ERR_RECORD_DOES_NOT_EXIST = "ORGTYPE-ROLE-DAC-002";
    public static final String ERR_UNKNOWN_EXCEPTION = "ORGTYPE-ROLE-DAC-003";
    
    Response saveOrgTypeRole(final Request p0);
    
    Response getOrgTypeRole(final Request p0);
    
    Response search(final Request p0);
    
    Response removeOrgTypeRole(final Request p0);
    
    Response removeRelationshipByRoleIds(final Request p0);
    
    Response removeRelationshipByOrgTypeIds(final Request p0);
}

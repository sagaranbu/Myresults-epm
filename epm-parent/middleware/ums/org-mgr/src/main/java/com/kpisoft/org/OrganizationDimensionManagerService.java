package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface OrganizationDimensionManagerService extends MiddlewareService
{
    public static final String ERR_DIM_UNKNOWN_EXCEPTION = "DIM-000";
    public static final String ERR_DIM_DOES_NOT_EXIST = "DIM-101";
    public static final String ERR_VAL_INVALID_INPUT = "DIM-201";
    
    Response createOrganizationDimension(final Request p0);
    
    Response getOrganizationDimension(final Request p0);
    
    Response removeOrganizationDimension(final Request p0);
    
    Response updateOrganizationDimension(final Request p0);
    
    Response getAllOrganizationDimension(final Request p0);
    
    Response searchOrgDimension(final Request p0);
    
    Response searchOrgStructure(final Request p0);
}

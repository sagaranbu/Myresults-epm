package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface OrganizationStructureManagerService extends MiddlewareService
{
    public static final String ERR_STR_UNKNOWN_EXCEPTION = "STR-000";
    public static final String ERR_STR_DOES_NOT_EXIST = "STR-101";
    public static final String ERR_STR_INVALID_INPUT = "STR-201";
    
    Response createOrganizationDimensionStructure(final Request p0);
    
    Response updateOrganizationDimensionStructure(final Request p0);
    
    Response getOrganizationDimensionStructure(final Request p0);
    
    Response removeOrganizationDimensionStructure(final Request p0);
    
    Response getAllOrganizationStructure(final Request p0);
}

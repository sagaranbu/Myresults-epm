package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OrganizationDimensionDataService extends DataAccessService
{
    public static final String ERR_VAL_INVALID_INPUT = "DIM-DAC-301";
    public static final String ERR_DIM_UNKNOWN_EXCEPTION = "DIM-DAC-000";
    public static final String ERR_DIM_DOES_NOT_EXIST = "DIM-DAC-101";
    public static final String ERR_DIM_DIRTY_UPDATE_EXCEPTION = "DIM-DAC-201";
    
    Response getOrganizationDimension(final Request p0);
    
    Response removeOrganizationDimension(final Request p0);
    
    Response saveOrganizationDimension(final Request p0);
    
    Response getAllOrganizationDimension(final Request p0);
    
    Response searchOrgStructure(final Request p0);
    
    Response searchOrgDimension(final Request p0);
}

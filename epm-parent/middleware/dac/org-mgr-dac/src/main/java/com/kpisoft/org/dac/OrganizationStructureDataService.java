package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OrganizationStructureDataService extends DataAccessService
{
    public static final String ERR_STR_UNKNOWN_EXCEPTION = "DIM-DAC-000";
    public static final String ERR_INVALID_DATA = "DIM-DAC-001";
    public static final String ERR_DIM_STRUCTURE_DOES_NOT_EXIST = "STRUCTURE-101";
    
    Response saveOrganizationStructureDimension(final Request p0);
    
    Response getOrganizationDimensionStructure(final Request p0);
    
    Response removeOrganizationDimensionStructure(final Request p0);
    
    Response getAllOrganizationStructure(final Request p0);
}

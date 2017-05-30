package com.kpisoft.emp.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface DesignationDataService extends DataAccessService
{
    Response saveDesignation(final Request p0);
    
    Response getDesignation(final Request p0);
    
    Response updateDesignation(final Request p0);
    
    Response removeDesignation(final Request p0);
    
    Response getAllDesignations(final Request p0);
    
    Response searchDesignation(final Request p0);
}

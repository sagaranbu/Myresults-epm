package com.kpisoft.emp;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface DesignationManagerService extends MiddlewareService
{
    Response saveDesignation(final Request p0);
    
    Response getDesignation(final Request p0);
    
    Response updateDesignation(final Request p0);
    
    Response removeDesignation(final Request p0);
    
    Response getAllDesignations(final Request p0);
    
    Response searchDesignation(final Request p0);
}

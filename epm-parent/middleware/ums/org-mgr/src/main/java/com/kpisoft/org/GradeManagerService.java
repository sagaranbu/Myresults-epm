package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface GradeManagerService extends MiddlewareService
{
    Response getGradeById(final Request p0);
    
    Response createGrade(final Request p0);
    
    Response updateGrade(final Request p0);
    
    Response removeGrade(final Request p0);
    
    Response getAllGrade(final Request p0);
    
    Response searchGrade(final Request p0);
    
    Response getGradeCount(final Request p0);
}

package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface GradeDataService extends DataAccessService
{
    Response getGradeById(final Request p0);
    
    Response saveGrade(final Request p0);
    
    Response removeGrade(final Request p0);
    
    Response getAllGrade(final Request p0);
    
    Response searchGrade(final Request p0);
    
    Response getGradeCount(final Request p0);
}

package com.kpisoft.emp;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface ManagerLevelService extends MiddlewareService
{
    Response saveManagerLevel(final Request p0);
    
    Response getManagerLevel(final Request p0);
    
    Response updateManagerLevel(final Request p0);
    
    Response removeManagerLevel(final Request p0);
    
    Response getAllManagerLevels(final Request p0);
    
    Response searchManagerLevel(final Request p0);
}

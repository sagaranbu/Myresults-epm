package com.kpisoft.emp.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface ManagerLevelDataService extends DataAccessService
{
    Response saveManagerLevel(final Request p0);
    
    Response getManagerLevel(final Request p0);
    
    Response updateManagerLevel(final Request p0);
    
    Response removeManagerLevel(final Request p0);
    
    Response getAllManagerLevels(final Request p0);
    
    Response searchManagerLevel(final Request p0);
}

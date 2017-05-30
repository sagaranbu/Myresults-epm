package com.canopus.entity;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface SystemBaseManagerService extends MiddlewareService
{
    Response createMasterBase(final Request p0);
    
    Response updateMasterBase(final Request p0);
    
    Response deleteMasterBase(final Request p0);
    
    Response getMasterBaseById(final Request p0);
    
    Response getBaseByCategory(final Request p0);
    
    Response getBaseByCatagories(final Request p0);
    
    Response getSysMasterByIds(final Request p0);
    
    Response searchCatagory(final Request p0);
    
    Response searchMasterBase(final Request p0);
    
    Response reloadCache(final Request p0);
}

package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface SystemDataService extends DataAccessService
{
    Response removeBaseDataById(final Request p0);
    
    Response createBaseData(final Request p0);
    
    Response getBaseDataById(final Request p0);
    
    Response getAllBaseData(final Request p0);
    
    Response getBaseDataForCategory(final Request p0);
    
    Response getBaseDataForBothCategories(final Request p0);
    
    Response getSysMasterByIds(final Request p0);
    
    Response searchCatagory(final Request p0);
    
    Response searchMasterBase(final Request p0);
}

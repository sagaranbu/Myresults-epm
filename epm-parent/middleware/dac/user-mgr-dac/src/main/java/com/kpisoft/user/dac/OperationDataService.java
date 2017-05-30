package com.kpisoft.user.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OperationDataService extends DataAccessService
{
    Response getOperation(final Request p0);
    
    Response getAllOperations(final Request p0);
    
    Response createOperationHierarchy(final Request p0);
    
    Response deleteOperationHierarchy(final Request p0);
    
    Response search(final Request p0);
    
    Response searchOperationByIdList(final Request p0);
    
    Response getOperationsByPageCodes(final Request p0);
    
    Response getChildOperations(final Request p0);
}

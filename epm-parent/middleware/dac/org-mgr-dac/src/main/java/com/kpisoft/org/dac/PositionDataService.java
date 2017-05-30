package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

@Deprecated
public interface PositionDataService extends DataAccessService
{
    public static final String POS_DAC_SERVICE_ID = "DAC_SERVICE_POSITION";
    public static final String ERR_POS_UNKNOWN_EXCEPTION = "POS-DAC-000";
    public static final String ERR_POS_DOES_NOT_EXIST = "POS-DAC-101";
    public static final String ERR_POS_DATA_DOES_NOT_EXIST = "POS-DAC-102";
    
    Response createPosition(final Request p0);
    
    Response createPositions(final Request p0);
    
    Response getPosition(final Request p0);
    
    Response getAllPositions(final Request p0);
    
    Response deletePosition(final Request p0);
    
    Response search(final Request p0);
    
    Response addParent(final Request p0);
    
    Response removeParent(final Request p0);
    
    Response getParentRelationships(final Request p0);
    
    Response getParentRelationshipsByPositionId(final Request p0);
    
    Response getAllParentRelationships(final Request p0);
    
    Response searchPosition(final Request p0);
    
    Response getPositionsCount(final Request p0);
}

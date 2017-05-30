package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

@Deprecated
public interface PositionManagerService extends MiddlewareService
{
    public static final String ERR_POS_UNKNOWN_EXCEPTION = "POS-000";
    public static final String ERR_INVALID_INPUT = "POS-001";
    public static final String ERR_POS_DOES_NOT_EXIST = "POS-101";
    public static final String OPERATION_FAILED = "POS-200";
    public static final String ERR_VAL_INVALID_INPUT = "POS-201";
    public static final String ERR_INVALID_CHILD_INPUT = "POS-202";
    public static final String ERR_INVALID_PARENT_INPUT = "POS-203";
    
    Response createPosition(final Request p0);
    
    Response createPositions(final Request p0);
    
    Response updatePosition(final Request p0);
    
    Response getPosition(final Request p0);
    
    Response getAllPositions(final Request p0);
    
    Response deletePosition(final Request p0);
    
    @Deprecated
    Response mergePositions(final Request p0);
    
    Response addParent(final Request p0);
    
    Response getParents(final Request p0);
    
    Response getChildrens(final Request p0);
    
    Response removeParent(final Request p0);
    
    Response getAscendants(final Request p0);
    
    Response getAscendantsByIdList(final Request p0);
    
    Response getDescendants(final Request p0);
    
    Response getDescendantsByIdList(final Request p0);
    
    Response isAscendant(final Request p0);
    
    Response isDescendant(final Request p0);
    
    Response isParent(final Request p0);
    
    Response isChild(final Request p0);
    
    Response getParentRelationships(final Request p0);
    
    Response getAllParentRelationships(final Request p0);
    
    @Deprecated
    Response searchPosition(final Request p0);
    
    Response getAscendantsGraph(final Request p0);
    
    Response getDescendantsGraph(final Request p0);
    
    Response getPositionsCount(final Request p0);
    
    Response getAscendantsGraphByIdList(final Request p0);
    
    Response getDescendantsGraphByIdList(final Request p0);
    
    Response search(final Request p0);
}

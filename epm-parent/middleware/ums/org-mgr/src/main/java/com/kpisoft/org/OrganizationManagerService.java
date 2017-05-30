package com.kpisoft.org;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface OrganizationManagerService extends MiddlewareService
{
    public static final String ERR_ORG_UNKNOWN_EXCEPTION = "ORG-000";
    public static final String ERR_ORG_DOES_NOT_EXIST = "ORG-101";
    public static final String ERR_INVALID_INPUT = "ORG-200";
    public static final String ERR_VAL_INVALID_INPUT = "ORG-201";
    public static final String ERR_INVALID_CHILD_INPUT = "ORG-202";
    public static final String ERR_INVALID_PARENT_INPUT = "ORG-203";
    public static final String ERR_NO_DEFAULT_DIMENSION = "ORG-301";
    public static final String ERR_MULTIPLE_DEFAULT_DIMENSIONS = "ORG-302";
    public static final String ERR_DELETE_ORG = "ORG-401";
    public static final String ERR_SUSPEND_ORG = "ORG-402";
    
    Response createOrganizationUnit(final Request p0);
    
    Response updateOrganizationUnit(final Request p0);
    
    Response getOrganizationUnit(final Request p0);
    
    Response getOrganizationUnits(final Request p0);
    
    @Deprecated
    Response mergeOrganizationUnits(final Request p0);
    
    Response searchOrganizationUnitByName(final Request p0);
    
    Response search(final Request p0);
    
    Response searchAll(final Request p0);
    
    Response deleteOrgUnit(final Request p0);
    
    Response suspendOrgUnit(final Request p0);
    
    Response addParent(final Request p0);
    
    Response getParents(final Request p0);
    
    Response getChildrens(final Request p0);
    
    Response removeParent(final Request p0);
    
    Response getParentRelationships(final Request p0);
    
    Response getAllParentRelationships(final Request p0);
    
    Response getAscendants(final Request p0);
    
    Response getAscendantsByIdList(final Request p0);
    
    Response getDescendants(final Request p0);
    
    Response getDescendantsByIdList(final Request p0);
    
    Response isAscendant(final Request p0);
    
    Response isDescendant(final Request p0);
    
    Response isParent(final Request p0);
    
    Response isChild(final Request p0);
    
    Response getParentsByDimension(final Request p0);
    
    Response getChildrensByDimension(final Request p0);
    
    Response getAscendantsByDimension(final Request p0);
    
    Response getDescendantsByDimension(final Request p0);
    
    Response suspendParentRelationshipsByIds(final Request p0);
    
    Response updateOrgPasswordPolicy(final Request p0);
    
    Response getDefaultPasswordPolicy(final Request p0);
    
    Response getOrgPasswordPolicy(final Request p0);
    
    Response validatePassword(final Request p0);
    
    Response generatePassword(final Request p0);
    
    Response searchDepartment(final Request p0);
    
    Response getOrganizationsByIdList(final Request p0);
    
    Response searchOrgByFieldData(final Request p0);
    
    Response getAscendantsGraph(final Request p0);
    
    Response getDescendantsGraph(final Request p0);
    
    Response getAscendantsGraphByDimension(final Request p0);
    
    Response getDescendantsGraphByDimension(final Request p0);
    
    Response getAscendantsGraphByIdList(final Request p0);
    
    Response getDescendantsGraphByIdList(final Request p0);
    
    Response getOrgBaseByIds(final Request p0);
    
    Response updateOrganizationUnitInCache(final Request p0);
    
    Response reloadCache(final Request p0);
}

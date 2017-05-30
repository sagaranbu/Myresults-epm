package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OrganizationUnitDataService extends DataAccessService
{
    public static final String ORG_DAC_SERVICE_ID = "DAC_SERVICE_ORGANIZATION_UNIT";
    public static final String ERR_ORG_UNKNOWN_EXCEPTION = "ORG-DAC-000";
    public static final String ERR_ORG_DOES_NOT_EXIST = "ORG-DAC-101";
    public static final String ERR_ORGS_DOES_NOT_EXIST = "ORG-DAC-102";
    public static final String ERR_ORG_PASSWORD_POLICY_DOES_NOT_EXIST = "ORG-DAC-103";
    
    Response saveOrganizationUnit(final Request p0);
    
    Response getOrganizationUnitData(final Request p0);
    
    Response getOrganizationUnits(final Request p0);
    
    Response deleteOrganizationUnit(final Request p0);
    
    Response suspendOrganizationUnit(final Request p0);
    
    Response deleteOrganizationsByIds(final Request p0);
    
    Response suspendOrganizationsByIds(final Request p0);
    
    Response searchOrganizationUnitByName(final Request p0);
    
    Response search(final Request p0);
    
    Response searchAll(final Request p0);
    
    Response addParent(final Request p0);
    
    Response getParentRelById(final Request p0);
    
    Response removeParent(final Request p0);
    
    Response getParentRelationshipsByOrgId(final Request p0);
    
    Response getParentRelationships(final Request p0);
    
    Response getAllParentRelationships(final Request p0);
    
    Response suspendParentRelationshipsByIds(final Request p0);
    
    Response updateOrgPasswordPolicy(final Request p0);
    
    Response getDefaultPasswordPolicy(final Request p0);
    
    Response getOrgPasswordPolicy(final Request p0);
    
    Response searchDepartment(final Request p0);
    
    Response getOrganizationsByIdList(final Request p0);
    
    Response searchOrgByFieldData(final Request p0);
    
    Response getOrgBaseByIds(final Request p0);
    
    Response loadOrgUnitsToCache(final Request p0);
}

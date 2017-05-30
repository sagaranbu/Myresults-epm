package com.kpisoft.emp;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface EmployeeManagerService extends MiddlewareService
{
    Response getEmployee(final Request p0);
    
    Response createEmployee(final Request p0);
    
    Response updateEmployee(final Request p0);
    
    Response deleteEmployee(final Request p0);
    
    Response deleteEmployeesByIds(final Request p0);
    
    Response suspendEmployeesByIds(final Request p0);
    
    Response search(final Request p0);
    
    Response searchAll(final Request p0);
    
    Response addSupervisor(final Request p0);
    
    Response setPrimarySupervisor(final Request p0);
    
    Response removeSupervisor(final Request p0);
    
    Response getSupervisorRelationships(final Request p0);
    
    Response getAllSupervisorRelationships(final Request p0);
    
    Response suspendSupervisorRelationships(final Request p0);
    
    Response suspendSupervisorRelationshipsByEmployeeIds(final Request p0);
    
    Response getPrimarySupervisor(final Request p0);
    
    Response getSupervisors(final Request p0);
    
    Response getSubordinates(final Request p0);
    
    Response getPrimarySubordinates(final Request p0);
    
    Response getPeers(final Request p0);
    
    Response getPrimaryPeers(final Request p0);
    
    Response getDescendants(final Request p0);
    
    Response getPrimaryDescendants(final Request p0);
    
    Response getAscendants(final Request p0);
    
    Response getPrimaryAscendants(final Request p0);
    
    Response isSupervisor(final Request p0);
    
    Response isAscendant(final Request p0);
    
    Response isSubordinate(final Request p0);
    
    Response isDescendant(final Request p0);
    
    Response addEmployeePosition(final Request p0);
    
    Response getEmployeePositions(final Request p0);
    
    Response removeEmployeePosition(final Request p0);
    
    Response getEmployeePositionRelationships(final Request p0);
    
    Response getAllEmployeePositionRelationships(final Request p0);
    
    Response suspendEmpPositionRelationships(final Request p0);
    
    Response suspendEmpPositionRelationshipsByEmployeeIds(final Request p0);
    
    Response addEmployeeOrganization(final Request p0);
    
    Response getEmployeeOrganizations(final Request p0);
    
    Response getOrganizationEmployees(final Request p0);
    
    Response removeEmployeeOrganization(final Request p0);
    
    Response getEmployeeOrganizationRelationships(final Request p0);
    
    Response getAllEmployeeOrganizationRelationships(final Request p0);
    
    Response suspendEmpOrgRelationships(final Request p0);
    
    Response suspendEmpOrgRelationshipsByEmployeeIds(final Request p0);
    
    Response employeeResignation(final Request p0);
    
    Response transferEmployee(final Request p0);
    
    Response getEmpCategory(final Request p0);
    
    Response getAllEmpCategories(final Request p0);
    
    Response createEmployeeCategory(final Request p0);
    
    Response updateEmployeeCategory(final Request p0);
    
    Response deleteCategory(final Request p0);
    
    Response getEmployeesCount(final Request p0);
    
    Response getUsersIdEmailFromEmployeeIdList(final Request p0);
    
    Response getOrganizationListEmployees(final Request p0);
    
    Response getEmployeesByIds(final Request p0);
    
    Response getEmployeesForOrgId(final Request p0);
}

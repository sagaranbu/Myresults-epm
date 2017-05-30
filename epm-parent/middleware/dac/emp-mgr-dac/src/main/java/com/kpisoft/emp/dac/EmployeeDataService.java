package com.kpisoft.emp.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface EmployeeDataService extends DataAccessService
{
    Response getEmployee(final Request p0);
    
    Response saveEmployee(final Request p0);
    
    Response deleteEmployee(final Request p0);
    
    Response deleteEmployeesByIds(final Request p0);
    
    Response suspendEmployeesByIds(final Request p0);
    
    Response search(final Request p0);
    
    Response searchAll(final Request p0);
    
    Response addSupervisor(final Request p0);
    
    Response setPrimarySupervisor(final Request p0);
    
    Response removeSupervisor(final Request p0);
    
    Response suspendSupervisorRelationships(final Request p0);
    
    Response suspendSupervisorRelationshipsByEmployeeIds(final Request p0);
    
    Response getSupervisorRelationships(final Request p0);
    
    Response getAllSupervisorRelationships(final Request p0);
    
    Response addEmployeePosition(final Request p0);
    
    Response removeEmployeePosition(final Request p0);
    
    Response getEmployeePositionRelationships(final Request p0);
    
    Response getAllEmployeePositionRelationships(final Request p0);
    
    Response suspendEmpPositionRelationships(final Request p0);
    
    Response suspendEmpPositionRelationshipsByEmployeeIds(final Request p0);
    
    Response addEmployeeOrganization(final Request p0);
    
    Response removeEmployeeOrganization(final Request p0);
    
    Response getEmployeeOrganizationRelationships(final Request p0);
    
    Response getAllEmployeeOrganizationRelationships(final Request p0);
    
    Response suspendEmpOrgRelationships(final Request p0);
    
    Response suspendEmpOrgRelationshipsByEmployeeIds(final Request p0);
    
    Response employeeResignation(final Request p0);
    
    Response transferEmployee(final Request p0);
    
    Response createCategory(final Request p0);
    
    Response getCategory(final Request p0);
    
    Response getAllCategories(final Request p0);
    
    Response deleteCategory(final Request p0);
    
    Response updateEmployeeCategory(final Request p0);
    
    Response getEmployeesCount(final Request p0);
    
    Response loadFrequentEmployeesToCache(final Request p0);
    
    Response getUsersIdEmailFromEmployeeIdList(final Request p0);
    
    Response getEmployeesByIds(final Request p0);
    
    Response getEmployeesForOrgId(final Request p0);
}

package com.kpisoft.emp.impl.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;
import java.util.*;

public class OrganizationRelationship extends BaseRelationship
{
    public OrganizationRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addEmployeeOrganization(final Integer empId, final Integer orgId) {
        if (this.isEmpAvailable(empId) && this.isOrgAvailable(orgId)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empId, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.get(orgId, (Class)EmployeeGraph.Organization.class);
            employee.addOrganization(org);
        }
    }
    
    public List<Integer> getEmployeeOrganizations(final Integer empId) {
        final List<Integer> result = new ArrayList<Integer>();
        if (this.isEmpAvailable(empId)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empId, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.OrganizationAssignment iterator : employee.getOrganization()) {
                result.add(iterator.getOrganization().getOrganizationId());
            }
        }
        return result;
    }
    
    public List<Integer> getOrganizationEmployees(final Integer orgId) {
        final List<Integer> result = new ArrayList<Integer>();
        if (this.isOrgAvailable(orgId)) {
            final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.get(orgId, (Class)EmployeeGraph.Organization.class);
            for (final EmployeeGraph.AssignedOrganization iterator : org.getAssignedOrganization()) {
                result.add(iterator.getEmployee().getEmployeeId());
            }
        }
        return result;
    }
    
    public List<Integer> getOrganizationListEmployees(final List<Integer> orgIdsList) {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < orgIdsList.size(); ++i) {
            if (this.isOrgAvailable(orgIdsList.get(i))) {
                final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.get((Integer)orgIdsList.get(i), (Class)EmployeeGraph.Organization.class);
                for (final EmployeeGraph.AssignedOrganization iterator : org.getAssignedOrganization()) {
                    result.add(iterator.getEmployee().getEmployeeId());
                }
            }
        }
        return result;
    }
    
    public void removeEmployeeOrganization(final Integer empId, final Integer orgId) {
        if (this.isEmpAvailable(empId)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empId, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.OrganizationAssignment iterator : employee.getOrganization()) {
                if (iterator.getOrganization().getOrganizationId().equals(orgId)) {
                    employee.removeOrganizationAssignment(iterator);
                }
            }
        }
    }
    
    public boolean isHou(final Integer empId, final Integer orgId) {
        if (this.isEmpAvailable(empId)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empId, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.OrganizationAssignment iterator : employee.getOrganization()) {
                if (iterator.getOrganization().getOrganizationId().equals(orgId) && iterator.getOrganization().isHod()) {
                    employee.removeOrganizationAssignment(iterator);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isEmpAvailable(final Integer id) {
        try {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            return id.equals(employee.getEmployeeId());
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean isOrgAvailable(final Integer id) {
        try {
            final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.get(id, (Class)EmployeeGraph.Organization.class);
            return id.equals(org.getOrganizationId());
        }
        catch (Exception e) {
            return false;
        }
    }
}

package com.kpisoft.emp.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.emp.vo.param.*;
import com.canopus.mw.utils.*;
import com.kpisoft.emp.utility.*;
import com.canopus.mw.dto.*;
import com.kpisoft.emp.impl.graph.*;
import com.kpisoft.emp.dac.*;
import javax.validation.*;
import java.util.*;
import com.kpisoft.emp.vo.*;

public class Employee extends BaseDomainObject
{
    private EmployeeManager manager;
    private EmployeeData employeeDetails;
    
    public Employee(final EmployeeManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(EMPParams.EMP_DATA.name(), (BaseValueObject)this.employeeDetails);
        final Response response = this.getDataService().saveEmployee(request);
        final Identifier identifier = (Identifier)response.get(EMPParams.EMP_ID.name());
        this.employeeDetails.setId(identifier.getId());
        return identifier.getId();
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.employeeDetails, EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid employee details");
    }
    
    public void setPrimarySupervisor(final Integer subID, final Integer supID) {
        final Request request = new Request();
        request.put(EMPParams.SUP_EMP_ID.name(), (BaseValueObject)new Identifier(supID));
        request.put(EMPParams.SUB_EMP_ID.name(), (BaseValueObject)new Identifier(subID));
        final Response response = this.getDataService().setPrimarySupervisor(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (status.isResponse()) {
            this.manager.getSupervisorRelationship().setPrimarySupervisor(subID, supID);
        }
    }
    
    public EmployeeIdentity getPrimarySupervisor(final Integer id) {
        final EmployeeGraph.Employee employee = this.manager.getSupervisorRelationship().getPrimarySupervisor(id);
        return this.toIdentity(employee);
    }
    
    public List<EmployeeIdentity> getSupervisors(final Integer subID) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getSupervisors(subID);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getSubordinates(final Integer id) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getSubordinates(id);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getPrimarySubordinates(final Integer id) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getPrimarySubordinates(id);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getPeers(final Integer id) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getPeers(id);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getPrimaryPeers(final Integer id) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getPrimaryPeers(id);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getDescendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getDescendants(id, dist);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getPrimaryDescendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getPrimaryDescendants(id, dist);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getAscendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getAscendants(id, dist);
        return this.toIdentityList(emps);
    }
    
    public List<EmployeeIdentity> getPrimaryAscendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> emps = this.manager.getSupervisorRelationship().getPrimaryAscendants(id, dist);
        return this.toIdentityList(emps);
    }
    
    public boolean isSupervisor(final Integer supID, final Integer subID) {
        final boolean status = this.manager.getSupervisorRelationship().isSubordinate(subID, supID);
        return status;
    }
    
    public boolean isAscendant(final Integer supID, final Integer subID) {
        final boolean status = this.manager.getSupervisorRelationship().isAscendant(supID, subID);
        return status;
    }
    
    public boolean isSubordinate(final Integer subID, final Integer supID) {
        final boolean status = this.manager.getSupervisorRelationship().isSubordinate(subID, supID);
        return status;
    }
    
    public boolean isDescendant(final Integer subID, final Integer supID) {
        final boolean status = this.manager.getSupervisorRelationship().isDescendant(subID, supID);
        return status;
    }
    
    public List<Integer> getEmployeePositions(final Integer empID) {
        final List<Integer> result = this.manager.getPositionRelationship().getEmployeePositions(empID);
        return result;
    }
    
    public List<Integer> getEmployeeOrganizations(final Integer empId) {
        final List<Integer> result = this.manager.getOrgRelationship().getEmployeeOrganizations(empId);
        return result;
    }
    
    public EmployeeData getEmployeeDetails() {
        return this.employeeDetails;
    }
    
    public void setEmployeeDetails(final EmployeeData employeeDetails) {
        this.employeeDetails = employeeDetails;
    }
    
    private EmployeeDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
    
    private EmployeeIdentity toIdentity(final EmployeeGraph.Employee emp) {
        final EmployeeIdentity id = new EmployeeIdentity();
        id.setId(emp.getEmployeeId());
        id.setEmpCode(emp.getName());
        return id;
    }
    
    private List<EmployeeIdentity> toIdentityList(final List<EmployeeGraph.Employee> emps) {
        final List<EmployeeIdentity> empids = new ArrayList<EmployeeIdentity>();
        for (final EmployeeGraph.Employee emp : emps) {
            empids.add(this.toIdentity(emp));
        }
        return empids;
    }
    
    public Integer createEmployeeCategory(final CategoryData objCategoryData) {
        final Request request = new Request();
        request.put(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)objCategoryData);
        final Response response = this.getDataService().createCategory(request);
        final Identifier objIdentifier = (Identifier)response.get(EMPParams.CATEGORY_ID.name());
        return objIdentifier.getId();
    }
    
    public boolean updateEmployeeCategory(final CategoryData objCategoryData) {
        final Request request = new Request();
        request.put(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)objCategoryData);
        final Response response = this.getDataService().updateEmployeeCategory(request);
        final BooleanResponse isUpdate = (BooleanResponse)response.get(EMPParams.CATEGORY_DATA.name());
        return isUpdate.isResponse();
    }
    
    public boolean deleteCategory(final Integer id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(EMPParams.CATEGORY_ID.name(), (BaseValueObject)objIdentifier);
        final Response response = this.getDataService().deleteCategory(request);
        final BooleanResponse isUpdate = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        return isUpdate.isResponse();
    }
}

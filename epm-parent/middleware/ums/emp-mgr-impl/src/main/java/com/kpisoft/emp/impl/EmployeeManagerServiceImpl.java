package com.kpisoft.emp.impl;

import com.kpisoft.emp.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.events.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.mw.*;
import org.apache.commons.lang.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.*;
import com.kpisoft.emp.impl.domain.*;
import com.kpisoft.emp.impl.graph.*;
import com.tinkerpop.blueprints.*;
import java.util.*;
import com.kpisoft.emp.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ EmployeeManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class EmployeeManagerServiceImpl extends BaseMiddlewareBean implements EmployeeManagerService
{
    public static String EMP_MGR_SERVICE;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    protected IMiddlewareEventClient middlewareEventClient;
    private static final Logger log;
    
    public EmployeeManagerServiceImpl() {
        this.employeeManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createEmployee(Request request) {
        EmployeeData data = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        BooleanResponse isTenantUser = (BooleanResponse)request.get(UMSParams.IS_TENANT_USER.name());
        if (isTenantUser == null) {
            isTenantUser = new BooleanResponse(false);
        }
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (data.getUserName() == null || data.getUserName().trim().isEmpty()) {
                data.setUserName(data.getEmail());
            }
            if (StringUtils.isBlank(data.getDisplayName())) {
                data.setDisplayName(data.getFirstName() + " " + data.getLastName());
            }
            final Employee emp = this.getEmployeeManager().saveOrUpdateEmployee(data);
            final Identifier identifier = new Identifier();
            identifier.setId(emp.getEmployeeDetails().getId());
            final Employee employee = this.getEmployeeManager().getEmployee(identifier.getId());
            data = employee.getEmployeeDetails();
            final UserData userData = new UserData();
            userData.setDisplayName(data.getDisplayName());
            userData.setEmail(data.getEmail());
            userData.setEmployeeId(data.getId());
            userData.setEndDate(data.getEndDate());
            userData.setFileExtension(data.getFileExtension());
            userData.setFirstName(data.getFirstName());
            userData.setGender(data.getGender());
            userData.setImage(data.getImage());
            userData.setImageHeight(data.getImageHeight());
            userData.setImageWidth(data.getImageWidth());
            userData.setLastName(data.getLastName());
            userData.setMiddleName(data.getMiddleName());
            userData.setPassword(this.generatePassword());
            userData.setSalutation(data.getSalutation());
            userData.setStartDate(data.getStartDate());
            userData.setStatus(data.getStatus());
            userData.setUsageType(data.getUsageType());
            userData.setUserCode(data.getEmpCode());
            userData.setVersion(1);
            if (emp.getEmployeeDetails().getUserName() != null) {
                userData.setUserName(data.getUserName());
            }
            else {
                userData.setUserName(data.getEmail());
            }
            request = new Request();
            request.put(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
            request.put(UMSParams.IS_TENANT_USER.name(), (BaseValueObject)isTenantUser);
            final UserManagerService userManagerService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
            final Response response = userManagerService.createUser(request);
            if (response.getErrors().size() > 0) {
                return response;
            }
            if (data.getEmpSupData() != null && !data.getEmpSupData().isEmpty()) {
                for (final EmployeeSupervisorRelationshipData iterator : data.getEmpSupData()) {
                    this.employeeManager.addSupervisorInGraph(iterator);
                }
            }
            if (data.getPosData() != null && !data.getPosData().isEmpty()) {
                for (final EmployeePositionData iterator2 : data.getPosData()) {
                    this.employeeManager.addEmployeePositionInGraph(iterator2);
                }
            }
            if (data.getEmpOrgRelData() != null && !data.getEmpOrgRelData().isEmpty()) {
                for (final EmployeeOrgRelationshipData iterator3 : data.getEmpOrgRelData()) {
                    this.employeeManager.addEmployeeOrganizationInGraph(iterator3);
                }
            }
            return this.OK(EMPParams.EMP_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - createEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateEmployee(final Request request) {
        EmployeeData data = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee emp = this.getEmployeeManager().saveOrUpdateEmployee(data);
            final Identifier identifier = new Identifier();
            identifier.setId(emp.getEmployeeDetails().getId());
            final Employee employee = this.getEmployeeManager().getEmployee(identifier.getId());
            if (this.employeeManager.getSupervisorRelationship().isAvailable(identifier.getId())) {
                this.employeeManager.getSupervisorRelationship().removeVertex(identifier.getId(), (Class)EmployeeGraph.Employee.class);
            }
            if (this.employeeManager.getPositionRelationship().isEmpAvailable(identifier.getId())) {
                this.employeeManager.getPositionRelationship().removeVertex(identifier.getId(), (Class)EmployeeGraph.Position.class);
            }
            if (this.employeeManager.getOrgRelationship().isEmpAvailable(identifier.getId())) {
                this.employeeManager.getOrgRelationship().removeVertex(identifier.getId(), (Class)EmployeeGraph.Organization.class);
            }
            data = employee.getEmployeeDetails();
            if (data.getEmpSupData() != null && !data.getEmpSupData().isEmpty()) {
                for (final EmployeeSupervisorRelationshipData iterator : data.getEmpSupData()) {
                    this.employeeManager.addSupervisorInGraph(iterator);
                }
            }
            if (data.getPosData() != null && !data.getPosData().isEmpty()) {
                for (final EmployeePositionData iterator2 : data.getPosData()) {
                    this.employeeManager.addEmployeePositionInGraph(iterator2);
                }
            }
            if (data.getEmpOrgRelData() != null && !data.getEmpOrgRelData().isEmpty()) {
                for (final EmployeeOrgRelationshipData iterator3 : data.getEmpOrgRelData()) {
                    this.employeeManager.addEmployeeOrganizationInGraph(iterator3);
                }
            }
            return this.OK(EMPParams.EMP_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - updateEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployee(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee emp = this.getEmployeeManager().getEmployee(id.getId());
            final EmployeeData empData = emp.getEmployeeDetails();
            return this.OK(EMPParams.EMP_DATA.name(), (BaseValueObject)empData);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteEmployee(Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final Vertex vertex = this.employeeManager.getFramedGraph().getBaseGraph().getVertex((Object)id.getId());
            if (vertex != null) {
                List<EmployeeIdentity> emps = employee.getSubordinates(id.getId());
                if (!emps.isEmpty()) {
                    return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), "Unable to delete employee, since subordinate relationships exists"));
                }
                employee = this.getEmployeeManager().getEmployee(id.getId());
                emps = employee.getSupervisors(id.getId());
                if (!emps.isEmpty()) {
                    return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), "Unable to delete employee, since supervisor relationships exists"));
                }
            }
            final IdentifierList list = new IdentifierList((List)new ArrayList());
            list.getIdsList().add(id.getId());
            final Date endDate = new Date();
            this.employeeManager.suspendEmpPositionRelationshipsByEmployeeIds(list.getIdsList(), endDate, true);
            this.employeeManager.suspendEmpOrgRelationshipsByEmployeeIds(list.getIdsList(), endDate, true);
            final boolean status = this.employeeManager.deleteEmployee(id.getId());
            final UserManagerService userService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
            request = new Request();
            request.put(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)list);
            userService.deleteUsersByEmployeeIds(request);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - deleteEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteEmployeesByIds(Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (empIds == null || empIds.getIdsList() == null || empIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Date endDate = new Date();
        try {
            this.employeeManager.suspendSupervisorRelationshipsByEmployeeIds(empIds.getIdsList(), endDate, true);
            this.employeeManager.suspendEmpPositionRelationshipsByEmployeeIds(empIds.getIdsList(), endDate, true);
            this.employeeManager.suspendEmpOrgRelationshipsByEmployeeIds(empIds.getIdsList(), endDate, true);
            final UserManagerService userService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
            request = new Request();
            request.put(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)empIds);
            userService.deleteUsersByEmployeeIds(request);
            final boolean status = this.employeeManager.deleteEmployeesByIds(empIds.getIdsList());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - deleteEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendEmployeesByIds(Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (empIds == null || empIds.getIdsList() == null || empIds.getIdsList().isEmpty() || endDate == null || endDate.getDate() == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            this.employeeManager.suspendSupervisorRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            this.employeeManager.suspendEmpPositionRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            this.employeeManager.suspendEmpOrgRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            final UserManagerService userService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
            request = new Request();
            request.put(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)empIds);
            request.put(UMSParams.END_DATE.name(), (BaseValueObject)endDate);
            userService.suspendUsersByEmployeeIds(request);
            final boolean status = this.employeeManager.suspendEmployeesByIds(empIds.getIdsList(), endDate.getDate());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_SUSPEND_BY_IDS_009.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response search(final Request request) {
        final EmployeeData employeeData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (employeeData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final List<EmployeeData> result = this.getEmployeeManager().search(employeeData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - search() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchAll(final Request request) {
        final EmployeeData employeeData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (employeeData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final List<EmployeeData> result = this.employeeManager.searchAll(employeeData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - searchAll() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response addSupervisor(final Request request) {
        final EmployeeSupervisorRelationshipData data = (EmployeeSupervisorRelationshipData)request.get(EMPParams.EMP_SUP_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        if (data.getEmployeeId() == null || data.getEmployeeId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_INVALID_CHILD_INPUT_024.name(), "Invalid child details provided"));
        }
        if (data.getSupervisorId() == null || data.getSupervisorId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_INVALID_PARENT_INPUT_025.name(), "Invalid parent details provided"));
        }
        if (data.getSupervisorId() == data.getEmployeeId()) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Child and parent cannot be same"));
        }
        if (data.getStartDate() == null) {
            data.setStartDate(new Date());
        }
        try {
            final Integer id = this.getEmployeeManager().addSupervisor(data);
            return this.OK(EMPParams.EMP_SUP_REL_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - addSupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeSupervisor(final Request request) {
        final EmployeeSupervisorRelationshipData data = (EmployeeSupervisorRelationshipData)request.get(EMPParams.EMP_SUP_REL_DATA.name());
        if (data == null || data.getId() == null || data.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        if (data.getEmployeeId() == null || data.getEmployeeId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_INVALID_CHILD_INPUT_024.name(), "Invalid child details provided"));
        }
        if (data.getSupervisorId() == null || data.getSupervisorId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_INVALID_PARENT_INPUT_025.name(), "Invalid parent details provided"));
        }
        if (data.getSupervisorId() == data.getEmployeeId()) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Child and parent cannot be same"));
        }
        try {
            final boolean status = this.getEmployeeManager().removeSupervisor(data);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - removeSupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getSupervisorRelationships(final Request request) {
        try {
            final List<EmployeeSupervisorRelationshipData> data = this.employeeManager.getSupervisorRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getSupervisorRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_GET_SUP_RELS_011.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllSupervisorRelationships(final Request request) {
        try {
            final List<EmployeeSupervisorRelationshipData> data = this.employeeManager.getAllSupervisorRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getSupervisorRelationships() : " + e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_GET_ALL_SUPS_010.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendSupervisorRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<EmployeeSupervisorRelationshipData> relList = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
            for (final EmployeeSupervisorRelationshipData iterator : relList) {
                iterator.setEndDate(endDate.getDate());
            }
            final boolean status = this.employeeManager.suspendSupervisorRelationships(relList);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendSupervisorRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_SUSPEND_SUP_RELS_013.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendSupervisorRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (empIds == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.employeeManager.suspendSupervisorRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendSupervisorRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_SUSPEND_SUP_RELS_BY_EMP_IDS_012.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response setPrimarySupervisor(final Request request) {
        final Identifier subEmpId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        final Identifier superEmpId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        if (subEmpId == null || subEmpId.getId() == null || subEmpId.getId() <= 0 || superEmpId == null || superEmpId.getId() == null || superEmpId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_EMPLOYEE_DATA", "No data object in the request"));
        }
        if (subEmpId.getId() == superEmpId.getId()) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Child and parent cannot be same"));
        }
        try {
            final Employee emp = this.getEmployeeManager().getEmployee(subEmpId.getId());
            emp.setPrimarySupervisor(subEmpId.getId(), superEmpId.getId());
            return this.OK();
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - setPrimarySupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPrimarySupervisor(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_EMPLOYEE_DATA", "No data object in the request"));
        }
        try {
            final Employee emp = this.getEmployeeManager().getEmployee(id.getId());
            final EmployeeIdentity employee = emp.getPrimarySupervisor(id.getId());
            return this.OK(EMPParams.EMP_DATA.name(), (BaseValueObject)employee);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPrimarySupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getSupervisors(final Request request) {
        final Identifier subID = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (subID == null || subID.getId() == null || subID.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(subID.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getSupervisors(subID.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getSupervisors() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getSubordinates(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getSubordinates(id.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getSubordinates() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPrimarySubordinates(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getPrimarySubordinates(id.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPrimarySubordinates() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPeers(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getPeers(id.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPeers() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPrimaryPeers(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getPrimaryPeers(id.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPrimaryPeers() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendants(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Identifier distance = (Identifier)request.get(EMPParams.DISTANCE.name());
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getDescendants(id.getId(), distance.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getDescendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPrimaryDescendants(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Identifier distance = (Identifier)request.get(EMPParams.DISTANCE.name());
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getPrimaryDescendants(id.getId(), distance.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPrimaryDescendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendants(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        final Identifier distance = (Identifier)request.get(EMPParams.DISTANCE.name());
        if (id == null || distance == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getAscendants(id.getId(), distance.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getAscendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getPrimaryAscendants(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_ID.name());
        final Identifier distance = (Identifier)request.get(EMPParams.DISTANCE.name());
        if (id == null || distance == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(id.getId());
            final List<? extends BaseValueObject> emps = (List<? extends BaseValueObject>)employee.getPrimaryAscendants(id.getId(), distance.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)emps);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getPrimaryAscendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isSupervisor(final Request request) {
        final Identifier supId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        final Identifier subId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        if (supId == null || subId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(supId.getId());
            final boolean status = employee.isSupervisor(supId.getId(), subId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - isSupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isAscendant(final Request request) {
        final Identifier supId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        final Identifier subId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        if (supId == null || subId == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_EMPLOYEE_DATA", "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(supId.getId());
            final boolean status = employee.isAscendant(supId.getId(), subId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - isAscendant() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isSubordinate(final Request request) {
        final Identifier supId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        final Identifier subId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        if (supId == null || subId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(supId.getId());
            final boolean status = employee.isSubordinate(subId.getId(), supId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - isAscendant() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isDescendant(final Request request) {
        final Identifier supId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        final Identifier subId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        if (supId == null || subId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(supId.getId());
            final boolean status = employee.isDescendant(supId.getId(), subId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - isDescendant() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response addEmployeePosition(final Request request) {
        final EmployeePositionData data = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (data.getStartDate() == null) {
                data.setStartDate(new Date());
            }
            final Integer id = this.getEmployeeManager().addEmployeePosition(data);
            return this.OK(EMPParams.EMP_POSITION_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - addEmployeePosition() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeePositions(final Request request) {
        final Identifier empID = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (empID == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(empID.getId());
            final List<Integer> result = employee.getEmployeePositions(empID.getId());
            final IdentifierList list = new IdentifierList((List)result);
            return this.OK(EMPParams.EMP_POSITION_ID_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeePositions() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeEmployeePosition(final Request request) {
        final EmployeePositionData data = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (data == null || data.getId() == null || data.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.getEmployeeManager().removeEmployeePosition(data);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - removeEmployeePosition() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeePositionRelationships(final Request request) {
        try {
            final List<EmployeePositionData> result = this.employeeManager.getEmployeePositionRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeePositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEmployeePositionRelationships(final Request request) {
        try {
            final List<EmployeePositionData> result = this.employeeManager.getAllEmployeePositionRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getAllEmployeePositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendEmpPositionRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_POSITION_DATA_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<EmployeePositionData> data = (List<EmployeePositionData>)list.getValueObjectList();
            for (final EmployeePositionData iterator : data) {
                iterator.setEndDate(endDate.getDate());
            }
            final boolean status = this.employeeManager.suspendEmpPositionRelationships(data);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendEmpPositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendEmpPositionRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (empIds == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.employeeManager.suspendEmpPositionRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendEmpPositionRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response addEmployeeOrganization(final Request request) {
        final EmployeeOrgRelationshipData data = (EmployeeOrgRelationshipData)request.get(EMPParams.EMP_ORG_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (data.getStartDate() == null) {
                data.setStartDate(new Date());
            }
            final Integer id = this.getEmployeeManager().addEmployeeOrganization(data);
            return this.OK(EMPParams.EMP_ORG_REL_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - addEmployeeOrganization() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_ADD_EMP_TO_ORG_014.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeeOrganizations(final Request request) {
        final Identifier empId = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (empId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Employee employee = this.getEmployeeManager().getEmployee(empId.getId());
            final List<Integer> result = employee.getEmployeeOrganizations(empId.getId());
            final IdentifierList list = new IdentifierList((List)result);
            final Response response = this.OK(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)list);
            response.put(EMPParams.ORG_ID_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeeOrganizations() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrganizationEmployees(final Request request) {
        final Identifier orgId = (Identifier)request.get(EMPParams.EMP_ORG_ID.name());
        if (orgId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<Integer> result = this.getEmployeeManager().getOrganizationEmployees(orgId.getId());
            final IdentifierList list = new IdentifierList((List)result);
            return this.OK(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getOrganizationEmployees() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeEmployeeOrganization(final Request request) {
        final EmployeeOrgRelationshipData data = (EmployeeOrgRelationshipData)request.get(EMPParams.EMP_ORG_REL_DATA.name());
        if (data == null || data.getId() == null || data.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.getEmployeeManager().removeEmployeeOrganization(data);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - removeEmployeeOrganization() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeeOrganizationRelationships(final Request request) {
        try {
            final List<EmployeeOrgRelationshipData> result = this.employeeManager.getEmployeeOrganizationRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeeOrganizationRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_GET_EMP_ORG_RELS_019.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEmployeeOrganizationRelationships(final Request request) {
        try {
            final List<EmployeeOrgRelationshipData> result = this.employeeManager.getAllEmployeeOrganizationRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getAllEmployeeOrganizationRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_GET_ALL_EMP_ORG_RELS_016.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendEmpOrgRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<EmployeeOrgRelationshipData> data = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
            for (final EmployeeOrgRelationshipData iterator : data) {
                iterator.setEndDate(endDate.getDate());
            }
            final boolean status = this.employeeManager.suspendEmpOrgRelationships(data);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendEmpOrgRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_SUSPEND_EMP_ORG_RELS_017.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendEmpOrgRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (empIds == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.employeeManager.suspendEmpOrgRelationshipsByEmployeeIds(empIds.getIdsList(), endDate.getDate(), false);
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - suspendEmpOrgRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_SUSPEND_EMP_ORG_RELS_BY_EMP_IDS_018.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response employeeResignation(final Request request) {
        final Identifier empPositionId = (Identifier)request.get(EMPParams.EMP_POSITION_ID.name());
        final DateResponse date = (DateResponse)request.get(EMPParams.POSITION_END_DATE.name());
        if (empPositionId == null || date == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            final boolean status = this.getEmployeeManager().employeeResignation(empPositionId.getId(), date.getDate());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - employeeResignation() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_RESGIN_020.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response transferEmployee(final Request request) {
        final EmployeePositionData oldPositionData = (EmployeePositionData)request.get(EMPParams.EMP_OLD_POSITION_DATA.name());
        final EmployeePositionData newPositionData = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (oldPositionData == null || newPositionData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            final boolean status = this.getEmployeeManager().transferEmployee(oldPositionData, newPositionData);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - transferEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_TRANSFER_021.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public String randomString(final int len, final String sample) {
        final Random rnd = new Random();
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            sb.append(sample.charAt(rnd.nextInt(sample.length())));
        }
        return sb.toString();
    }
    
    public String generatePassword() {
        return "m1user123";
    }
    
    public Response getEmpCategory(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.CATEGORY_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final CategoryData data = this.employeeManager.getEmpCategory(identifier.getId());
            return this.OK(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmpCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEmpCategories(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<CategoryData> categoryData = this.employeeManager.getAllEmpCategories(sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)categoryData);
            final Response response = this.OK(EMPParams.CATEGORY_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getAllEmpCategories() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createEmployeeCategory(final Request request) {
        final CategoryData objCategoryData = (CategoryData)request.get(EMPParams.CATEGORY_DATA.name());
        if (objCategoryData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No Employee Category Data Found in request "));
        }
        try {
            final Integer employmentCategoryId = this.getEmployeeManager().createEmployeeCategory(objCategoryData);
            return this.OK(EMPParams.CATEGORY_ID.name(), (BaseValueObject)new Identifier(employmentCategoryId));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - createEmployeeCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateEmployeeCategory(final Request request) {
        final CategoryData objCategoryData = (CategoryData)request.get(EMPParams.CATEGORY_DATA.name());
        if (objCategoryData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_DOES_NOT_EXIST_001.name(), "No Employee Category Data Found in request "));
        }
        try {
            final boolean isUpdate = this.getEmployeeManager().updateEmployeeCategory(objCategoryData);
            return this.OK(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)new BooleanResponse(isUpdate));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - updateEmployeeCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteCategory(final Request request) {
        final Identifier objIdentifier = (Identifier)request.get(EMPParams.CATEGORY_ID.name());
        if (objIdentifier == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No Employee Category Data Found in request "));
        }
        try {
            final boolean isUpdate = this.getEmployeeManager().deleteCategory(objIdentifier.getId());
            return this.OK(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)new BooleanResponse(isUpdate));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - deleteCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrganizationListEmployees(final Request request) {
        final IdentifierList orgIdList = (IdentifierList)request.get(EMPParams.EMP_ORG_ID_LIST.name());
        if (orgIdList == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<Integer> result = this.getEmployeeManager().getOrganizationListEmployees(orgIdList.getIdsList());
            final IdentifierList list = new IdentifierList((List)result);
            return this.OK(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getOrganizationEmployees() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public EmployeeManager getEmployeeManager() {
        return this.employeeManager;
    }
    
    public void setEmployeeManager(final EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }
    
    public Response getEmployeesCount(final Request request) {
        try {
            final Double totalEmployees = this.getEmployeeManager().getEmployeesCount();
            return this.OK(EMPParams.EMP_COUNT.name(), (BaseValueObject)new DoubleIdentifier(totalEmployees));
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeesCount() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getUsersIdEmailFromEmployeeIdList(final Request request) {
        final IdentifierList empIdList = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (empIdList == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectMap baseValueObjectMap = this.employeeManager.getUsersIdEmailFromEmployeeIdList(empIdList);
            return this.OK(EMPParams.USER_DATA_ID_EMAIL.name(), (BaseValueObject)baseValueObjectMap);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getUsersIdEmailFromEmployeeIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_GET_USERS_ROLE_LIST_023.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeesByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No Data found"));
        }
        try {
            final List<EmployeeData> empList = this.employeeManager.getEmployeesByIds(idList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)empList);
            return this.OK(EMPParams.EMP_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), "unknown error in getEmployeesByIds", (Throwable)e));
        }
    }
    
    public Response getEmployeesForOrgId(final Request request) {
        final IdentifierList orgIds = (IdentifierList)request.get(EMPParams.ORG_ID_LIST.name());
        if (orgIds == null || orgIds.getIdsList() == null || orgIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid input in request"));
        }
        try {
            final List<EmployeeData> result = this.employeeManager.getEmployeesForOrgId(orgIds.getIdsList());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeManagerServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        EmployeeManagerServiceImpl.EMP_MGR_SERVICE = "EmployeeManagerService";
        log = LoggerFactory.getLogger((Class)EmployeeManagerServiceImpl.class);
    }
}

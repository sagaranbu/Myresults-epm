package com.kpisoft.org.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import com.kpisoft.org.graph.*;
import com.kpisoft.emp.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.org.vo.*;
import com.platinum.dpv.*;
import edu.vt.middleware.password.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.tinkerpop.blueprints.*;
import com.kpisoft.org.*;
import org.perf4j.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OrganizationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrganizationManagerServiceImpl extends BaseMiddlewareBean implements OrganizationManagerService
{
    public static String ORG_MGR_SERVICE;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public OrganizationManagerServiceImpl() {
        this.organizationManager = null;
        this.serviceLocator = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createOrganizationUnit(final Request request) {
        OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        data = this.fireBeforeCreate(request, data);
        final Integer dimensionId = null;
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().createOrganizationUnit(data, dimensionId);
            final Identifier identifier = new Identifier(orgUnit.getOrgUnitData().getId());
            final List<OrgParentRelationshipBean> identityRelData = this.organizationManager.getParentRelationshipsByOrgId(identifier.getId());
            if (identityRelData != null && !identityRelData.isEmpty()) {
                for (final OrgParentRelationshipBean iterator : identityRelData) {
                    this.organizationManager.addParentInGraph(iterator);
                }
            }
            return this.OK(OrganizationParams.ORG_IDENTIFIER.getParamName(), (BaseValueObject)identifier);
        }
        catch (DataAccessException ex) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - createOrganizationUnit() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to create organization unit", (Throwable)ex));
        }
        catch (ValidationException ve) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - createOrganizationUnit() : " + ve);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to create organization unit", (Throwable)ve));
        }
        catch (Exception ex2) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - createOrganizationUnit() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to create organization unit", (Throwable)ex2));
        }
    }
    
    public Response updateOrganizationUnit(final Request request) {
        OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        data = this.fireBeforeUpdate(request, data);
        final Integer dimensionId = null;
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().createOrganizationUnit(data, dimensionId);
            final Identifier identifier = new Identifier(orgUnit.getOrgUnitData().getId());
            final Vertex vertex = this.organizationManager.getIdentityFramedGraph().getBaseGraph().getVertex((Object)identifier.getId());
            if (vertex != null) {
                final List<OrgGraph.Organization> parents = this.organizationManager.getIdentityParentRelationship().getParents(identifier.getId());
                for (final OrgGraph.Organization parent : parents) {
                    final Integer parentID = parent.getOrgId();
                    this.organizationManager.removeParentFromGraph(data.getId(), parentID);
                }
                final OrgGraph.Organization child = (OrgGraph.Organization)this.organizationManager.getIdentityParentRelationship().add(identifier.getId(), (Class)OrgGraph.Organization.class);
                child.setOrgId(identifier.getId());
                child.setOrgCode(data.getOrgUnitCode());
                child.setOrgName(data.getOrgName());
                child.setOrgStartDate(data.getStartDate());
                if (data.getEndDate() != null) {
                    child.setOrgEndDate(data.getEndDate());
                }
                if (data.getLevel() != null) {
                    child.setLevel(data.getLevel());
                }
                if (data.getStatus() != null) {
                    child.setStatus(data.getStatus());
                }
                if (data.getOrgType() != null) {
                    child.setOrgType(data.getOrgType());
                }
            }
            final List<OrgParentRelationshipBean> identityRelData = this.organizationManager.getParentRelationshipsByOrgId(identifier.getId());
            if (identityRelData != null && !identityRelData.isEmpty()) {
                for (final OrgParentRelationshipBean iterator : identityRelData) {
                    this.organizationManager.addParentInGraph(iterator);
                }
            }
            return this.OK(OrganizationParams.ORG_IDENTIFIER.getParamName(), (BaseValueObject)identifier);
        }
        catch (DataAccessException ex) {
            ex.printStackTrace(System.out);
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - updateOrganizationUnit() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to update organization unit", (Throwable)ex));
        }
        catch (ValidationException ve) {
            ve.printStackTrace(System.out);
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - updateOrganizationUnit() : " + ve);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to update organization unit", (Throwable)ve));
        }
        catch (Exception ex2) {
            ex2.printStackTrace(System.out);
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - updateOrganizationUnit() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to update organization unit", (Throwable)ex2));
        }
    }
    
    public Response getOrganizationUnit(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final OrganizationUnitData data = orgUnit.getOrgUnitData();
            return this.OK(OrganizationParams.ORG_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (DataAccessException ex) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrganizationUnit() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to load organization unit", (Throwable)ex));
        }
        catch (Exception ex2) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrganizationUnit() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to load organization unit", (Throwable)ex2));
        }
    }
    
    public Response getOrganizationUnits(final Request request) {
        try {
            final List<OrganizationUnitData> orgUnitsList = this.getOrganizationManager().getOrganizationUnits();
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)orgUnitsList);
            return this.OK(OrganizationParams.ORG_DATA_LIST.getParamName(), (BaseValueObject)baseValueObjectList);
        }
        catch (DataAccessException ex) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrganizationUnits() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to load organization units", (Throwable)ex));
        }
        catch (Exception ex2) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrganizationUnits() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to load organization units", (Throwable)ex2));
        }
    }
    
    public Response mergeOrganizationUnits(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.getParamName());
        final IdentifierList ids = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName());
        if (data == null || ids == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final Request req = new Request();
            req.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)ids);
            this.organizationManager.getDataService().deleteOrganizationsByIds(req);
            final OrganizationUnit orgUnit = this.getOrganizationManager().createOrganizationUnit(data, null);
            return this.OK(OrganizationParams.ORG_DATA.getParamName(), (BaseValueObject)orgUnit.getOrgUnitData());
        }
        catch (DataAccessException ex) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - mergeOrganizationUnits() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to merge organization units", (Throwable)ex));
        }
        catch (Exception ex2) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - mergeOrganizationUnits() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to merge organization units", (Throwable)ex2));
        }
    }
    
    public Response searchOrganizationUnitByName(final Request request) {
        final Name name = (Name)request.get(OrganizationParams.ORG_UNIT_NAME.getParamName());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (name == null || name.getName() == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final List<OrganizationUnitData> orgUnits = this.getOrganizationManager().searchOrganizationUnitByName(name.getName(), page, sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)orgUnits);
            return this.OK(OrganizationParams.ORG_DATA_LIST.getParamName(), (BaseValueObject)result);
        }
        catch (DataAccessException ex) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - searchOrganizationUnitByName() : " + ex);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to search organization unit by name", (Throwable)ex));
        }
        catch (Exception ex2) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - searchOrganizationUnitByName() : " + ex2);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to search organization unit by name", (Throwable)ex2));
        }
    }
    
    public Response search(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final List<OrganizationUnitData> orgUnits = this.organizationManager.search(data, page, sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)orgUnits);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)result);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - search() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchAll(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final List<OrganizationUnitData> orgUnits = this.organizationManager.searchAll(data, page, sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)orgUnits);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)result);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - searchAll() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteOrgUnit(final Request request) {
        final Identifier orgId = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        final BooleanResponse deleteChilds = (BooleanResponse)request.get(OrganizationParams.DELETE_CHILDS.name());
        final BooleanResponse deleteEmployees = (BooleanResponse)request.get(OrganizationParams.DELETE_EMPLOYEES.name());
        this.fireBeforeDelete(request, orgId, deleteChilds, deleteEmployees);
        if (orgId == null || orgId.getId() == null || orgId.getId() <= 0 || deleteChilds == null || deleteEmployees == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-201", "Invalid input data in the request"));
        }
        if (!deleteChilds.isResponse() || !deleteEmployees.isResponse()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-401", "Unable to delete org unit"));
        }
        final DateResponse endDate = new DateResponse();
        endDate.setDate(new Date());
        try {
            final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
            this.recursiveOrgUnit(orgId.getId(), true, empService, endDate);
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - deleteOrgUnit() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendOrgUnit(final Request request) {
        final Identifier orgId = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        final DateResponse endDate = (DateResponse)request.get(OrganizationParams.END_DATE.name());
        final BooleanResponse suspendChild = (BooleanResponse)request.get(OrganizationParams.SUSPEND_CHILDS.name());
        final BooleanResponse suspendEmp = (BooleanResponse)request.get(OrganizationParams.SUSPEND_EMPLOYEES.name());
        this.fireBeforeSuspend(request, orgId, endDate, suspendChild, suspendEmp);
        if (orgId == null || orgId.getId() == null || orgId.getId() <= 0 || endDate == null || endDate.getDate() == null || suspendChild == null || suspendEmp == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-201", "Invalid input data in the request"));
        }
        if (!suspendChild.isResponse() || !suspendEmp.isResponse()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-402", "Unable to suspend org unit"));
        }
        try {
            final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
            this.recursiveOrgUnit(orgId.getId(), false, empService, endDate);
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - suspendOrgUnit() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    private void recursiveOrgUnit(final Integer orgId, final boolean delete, final EmployeeManagerService empService, final DateResponse endDate) {
        Request request = new Request();
        request.put(EMPParams.EMP_ORG_ID.name(), (BaseValueObject)new Identifier(orgId));
        final Response response = empService.getOrganizationEmployees(request);
        final IdentifierList empIds = (IdentifierList)response.get(EMPParams.EMP_ID_LIST.name());
        if (empIds != null && empIds.getIdsList() != null && !empIds.getIdsList().isEmpty()) {
            request = new Request();
            request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)empIds);
            request.put(EMPParams.END_DATE.name(), (BaseValueObject)endDate);
            if (delete) {
                empService.deleteEmployeesByIds(request);
            }
            else {
                empService.suspendEmployeesByIds(request);
            }
        }
        List<OrgIdentityBean> result = new ArrayList<OrgIdentityBean>();
        final Vertex vertex = this.organizationManager.getIdentityFramedGraph().getBaseGraph().getVertex((Object)orgId);
        if (vertex != null) {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(orgId);
            result = orgUnit.getChildrens(orgId);
            final List<Integer> idsList = new ArrayList<Integer>();
            idsList.add(orgId);
            this.organizationManager.suspendParentRelationshipsByIds(idsList, endDate.getDate(), delete);
            this.organizationManager.getIdentityParentRelationship().removeVertex(orgId, (Class)OrgGraph.Organization.class);
        }
        if (delete) {
            this.organizationManager.deleteOrganizationUnit(orgId);
        }
        else {
            this.organizationManager.suspendOrganizationUnit(orgId, endDate.getDate());
        }
        for (final OrgIdentityBean iterator : result) {
            this.recursiveOrgUnit(iterator.getId(), delete, empService, endDate);
        }
    }
    
    public Response addParent(final Request request) {
        final OrgParentRelationshipBean data = (OrgParentRelationshipBean)request.get(OrganizationParams.ORG_REL_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        if (data.getSourceIdentity() == null || data.getSourceIdentity().getId() == null || data.getSourceIdentity().getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-202", "Invalid child details provided"));
        }
        if (data.getDestinationIdentity() == null || data.getDestinationIdentity().getId() == null || data.getDestinationIdentity().getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-203", "Invalid parent details provided"));
        }
        if (data.getSourceIdentity().getId() == data.getDestinationIdentity().getId()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Child and parent cannot be same"));
        }
        if (data.getStartDate() == null) {
            data.setStartDate(new Date());
        }
        try {
            OrganizationUnit orgUnit = this.organizationManager.getOrganizationUnit(data.getSourceIdentity().getId());
            if (orgUnit.getOrgUnitData() == null) {
                return this.ERROR((Exception)new MiddlewareException("ORG-202", "Invalid child details provided"));
            }
            orgUnit = this.organizationManager.getOrganizationUnit(data.getDestinationIdentity().getId());
            if (orgUnit.getOrgUnitData() == null) {
                return this.ERROR((Exception)new MiddlewareException("ORG-203", "Invalid parent details provided"));
            }
            final Integer id = this.getOrganizationManager().addParent(data);
            return this.OK(OrganizationParams.ORG_REL_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (ValidationException ve) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - addParent() : " + ve);
            return this.ERROR((Exception)new MiddlewareException("ORG-201", ve.getMessage(), (Throwable)ve));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - addParent() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeParent(final Request request) {
        final OrgParentRelationshipBean data = (OrgParentRelationshipBean)request.get(OrganizationParams.ORG_REL_DATA.getParamName());
        if (data == null || data.getId() == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        if (data.getSourceIdentity() == null || data.getSourceIdentity().getId() == null || data.getSourceIdentity().getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-202", "Invalid child details provided"));
        }
        if (data.getDestinationIdentity() == null || data.getDestinationIdentity().getId() == null || data.getDestinationIdentity().getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-203", "Invalid parent details provided"));
        }
        if (data.getSourceIdentity().getId() == data.getDestinationIdentity().getId()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Child and parent cannot be same"));
        }
        try {
            final boolean status = this.getOrganizationManager().removeParent(data);
            return this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - removeParent() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getParentRelationships(final Request request) {
        try {
            final List<OrgParentRelationshipBean> data = this.organizationManager.getParentRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getParentRelationships() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllParentRelationships(final Request request) {
        try {
            final List<OrgParentRelationshipBean> data = this.organizationManager.getAllParentRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAllParentRelationships() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getParents(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getParents(identifier.getId());
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getParents() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getChildrens(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getChildrens(identifier.getId());
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getChildrens() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendants(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getAscendants(identifier.getId(), distance);
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendants() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendantsByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final List<OrgIdentityBean> result = this.organizationManager.getAscendantsByIdList(identifier.getIdsList(), distance);
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = this.organizationManager.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendantsByIdList() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendants(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getDescendants(identifier.getId(), distance);
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendants() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendantsByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final List<OrgIdentityBean> result = this.organizationManager.getDescendantsByIdList(identifier.getIdsList(), distance);
            final BaseValueObjectList identityList = new BaseValueObjectList();
            identityList.setValueObjectList((List)result);
            final List<Integer> idList = this.organizationManager.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)identityList);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendantsByIdList() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isAscendant(final Request request) {
        final Identifier childId = (Identifier)request.get(OrganizationParams.CHILD_ORG_ID.getParamName());
        final Identifier parentId = (Identifier)request.get(OrganizationParams.PARENT_ORG_ID.getParamName());
        if (childId == null || childId.getId() == null || childId.getId() <= 0 || parentId == null || parentId.getId() == null || parentId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(parentId.getId());
            final boolean status = orgUnit.isAscendant(parentId.getId(), childId.getId());
            return this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - isAscendant() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isDescendant(final Request request) {
        final Identifier childId = (Identifier)request.get(OrganizationParams.CHILD_ORG_ID.getParamName());
        final Identifier parentId = (Identifier)request.get(OrganizationParams.PARENT_ORG_ID.getParamName());
        if (childId == null || childId.getId() == null || childId.getId() <= 0 || parentId == null || parentId.getId() == null || parentId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(childId.getId());
            final boolean status = orgUnit.isDescendant(childId.getId(), parentId.getId());
            return this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - isDescendant() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isParent(final Request request) {
        final Identifier childId = (Identifier)request.get(OrganizationParams.CHILD_ORG_ID.getParamName());
        final Identifier parentId = (Identifier)request.get(OrganizationParams.PARENT_ORG_ID.getParamName());
        if (childId == null || childId.getId() == null || childId.getId() <= 0 || parentId == null || parentId.getId() == null || parentId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(parentId.getId());
            final boolean status = orgUnit.isParent(parentId.getId(), childId.getId());
            return this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - isParent() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response isChild(final Request request) {
        final Identifier childId = (Identifier)request.get(OrganizationParams.CHILD_ORG_ID.getParamName());
        final Identifier parentId = (Identifier)request.get(OrganizationParams.PARENT_ORG_ID.getParamName());
        if (childId == null || childId.getId() == null || childId.getId() <= 0 || parentId == null || parentId.getId() == null || parentId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(childId.getId());
            final boolean status = orgUnit.isChild(childId.getId(), parentId.getId());
            return this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - isChild() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getParentsByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.CHILD_ORG_ID.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getParentsByDimension(identifier.getId(), dimensionId.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)list);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getParentsByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getChildrensByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.PARENT_ORG_ID.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getChildrensByDimension(identifier.getId(), dimensionId.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)list);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getChildrensByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendantsByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getAscendantsByDimension(identifier.getId(), distance, dimensionId.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)list);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendantsByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendantsByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        final Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        int distance = 0;
        if (dist != null && dist.getId() != null && dist.getId() > 0) {
            distance = dist.getId();
        }
        try {
            final OrganizationUnit orgUnit = this.getOrganizationManager().getOrganizationUnit(identifier.getId());
            final List<OrgIdentityBean> result = orgUnit.getDescendantsByDimension(identifier.getId(), distance, dimensionId.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final List<Integer> idList = orgUnit.toIdList(result);
            final Response response = this.OK();
            response.put(OrganizationParams.ORG_IDENTITY_REL_LIST.name(), (BaseValueObject)list);
            response.put(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendantsByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response suspendParentRelationshipsByIds(final Request request) {
        final IdentifierList orgIds = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(OrganizationParams.END_DATE.name());
        if (orgIds == null || orgIds.getIdsList() == null || orgIds.getIdsList().isEmpty() || endDate == null || endDate.getDate() == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-201", "No input data in the request"));
        }
        try {
            final boolean status = this.organizationManager.suspendParentRelationshipsByIds(orgIds.getIdsList(), endDate.getDate(), false);
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - suspendParentRelationshipsByIds() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateOrgPasswordPolicy(final Request request) {
        final PasswordPolicyData policyData = (PasswordPolicyData)request.get(OrganizationParams.PASSWORD_POLICY_DATA.getParamName());
        if (policyData == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final Integer id = this.getOrganizationManager().updateOrgPasswordPolicy(policyData);
            return this.OK(OrganizationParams.PASSWORD_POLICY_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - updateOrgPasswordPolicy() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDefaultPasswordPolicy(final Request request) {
        try {
            final PasswordPolicyData policyData = this.getOrganizationManager().getDefaultPasswordPolicy();
            return this.OK(OrganizationParams.PASSWORD_POLICY_DATA.getParamName(), (BaseValueObject)policyData);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDefaultPasswordPolicy() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrgPasswordPolicy(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        try {
            final PasswordPolicyData policyData = this.getOrganizationManager().getOrganizationUnit(identifier.getId()).getOrgPasswordPolicy();
            return this.OK(OrganizationParams.PASSWORD_POLICY_DATA.getParamName(), (BaseValueObject)policyData);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrgPasswordPolicy() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response validatePassword(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        final StringIdentifier passwordIdentifier = (StringIdentifier)request.get(OrganizationParams.PASSWORD.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || passwordIdentifier == null || passwordIdentifier.getId() == null || passwordIdentifier.getId().trim().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        final PasswordPolicyData policyData = this.getOrganizationManager().getOrganizationUnit(identifier.getId()).getOrgPasswordPolicy();
        boolean status = true;
        final List<StatusResponse> failedValidations = new ArrayList<StatusResponse>();
        final LengthRule lengthRule = new LengthRule((int)policyData.getMinLength(), (int)policyData.getMaxLength());
        final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
        charRule.getRules().add(new DigitCharacterRule((int)policyData.getMinNumbers()));
        charRule.getRules().add(new NonAlphanumericCharacterRule((int)policyData.getMinSpecial()));
        charRule.getRules().add(new UppercaseCharacterRule((int)policyData.getMinCaps()));
        charRule.setNumberOfCharacteristics(charRule.getRules().size());
        final List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add((Rule)lengthRule);
        ruleList.add((Rule)charRule);
        final PasswordValidator passwordValidtor = new PasswordValidator((List)ruleList);
        final PasswordData passwordData = new PasswordData(new Password(passwordIdentifier.getId()));
        final RuleResult result = passwordValidtor.validate(passwordData);
        if (!result.isValid()) {
            final List<String> messages = (List<String>)passwordValidtor.getMessages(result);
            for (int index = 0; index < messages.size() - 1; ++index) {
                status = false;
                failedValidations.add(new StatusResponse((String)messages.get(index)));
            }
        }
        DictionaryPasswordValidator dictionaryValidator = null;
        try {
            dictionaryValidator = DictionaryPasswordValidator.getInstance();
            if (dictionaryValidator.isDictionaryWord(passwordIdentifier.getId())) {
                status = false;
                failedValidations.add(new StatusResponse("Password contains dictionary word"));
            }
            else if (dictionaryValidator.isPasswordDictionaryBased(passwordIdentifier.getId())) {
                status = false;
                failedValidations.add(new StatusResponse("Password contains dictionary word"));
            }
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to validate password", (Throwable)ex));
        }
        final BooleanResponse booleanResponse = new BooleanResponse(status);
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)failedValidations);
        final Response response = this.OK(OrganizationParams.STATUS.getParamName(), (BaseValueObject)booleanResponse);
        response.put(OrganizationParams.VALIDATION_MSG_LIST.getParamName(), (BaseValueObject)list);
        return response;
    }
    
    public String randomString(final int len, final String sample) {
        final Random rnd = new Random();
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            sb.append(sample.charAt(rnd.nextInt(sample.length())));
        }
        return sb.toString();
    }
    
    public Response generatePassword(final Request request) {
        final int len = 8;
        final String sample = "01234!56789!AB$CDEFG^HIJK&MNOP*QRSTUV!WXYZabcdef@$ghijklmno$pq%rs&tu*vwxyz";
        final String result = this.randomString(len, sample);
        return this.OK(OrganizationParams.PASSWORD.getParamName(), (BaseValueObject)new StringIdentifier(result));
    }
    
    public Response searchDepartment(final Request request) {
        final OrganizationUnitData objOrganizationUnitData = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        if (objOrganizationUnitData == null) {
            throw new MiddlewareException("ORG-200", "No Org data found in request");
        }
        try {
            final List<OrganizationUnitData> alOrganizationUnitData = this.getOrganizationManager().searchDepartment(objOrganizationUnitData);
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)alOrganizationUnitData);
            return this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)objectList);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - serachDepartment() : " + e);
            return this.ERROR((Exception)new MiddlewareException("OPERATION_FAILED", "Unable to fetch the department data", (Throwable)e));
        }
    }
    
    public Response getOrganizationsByIdList(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No data object in the request"));
        }
        try {
            final List<OrganizationUnitData> orgData = this.getOrganizationManager().getOrganizationsByIdList(idList, page, sortList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)orgData);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)bList);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrganizationsByIdList() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrgBaseByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No data object in the request"));
        }
        try {
            final List<OrganizationUnitData> orgData = this.getOrganizationManager().getOrgBaseByIds(idList, page, sortList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)orgData);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)bList);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getOrgBaseByIds() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchOrgByFieldData(final Request request) {
        final OrganizationUnitData objOrganizationUnitData = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (objOrganizationUnitData == null) {
            throw new MiddlewareException("ORG-200", "No Org data found in request");
        }
        try {
            final List<OrganizationUnitData> alOrganizationUnitData = this.getOrganizationManager().searchOrgByFieldData(objOrganizationUnitData, page, sortList);
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)alOrganizationUnitData);
            return this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)objectList);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - searchOrgByFieldData() : " + e);
            return this.ERROR((Exception)new MiddlewareException("OPERATION_FAILED", "Unable to fetch the department data", (Throwable)e));
        }
    }
    
    public OrganizationManager getOrganizationManager() {
        return this.organizationManager;
    }
    
    public void setOrganizationManager(final OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }
    
    public Response getAscendantsGraph(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getAscendantsGraph(identifier.getId(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendantsGraph() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendantsGraph(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getDescendantsGraph(identifier.getId(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendantsGraph() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendantsGraphByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getAscendantsGraphByDimension(identifier.getId(), dist.getId(), dimensionId.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendantsGraphByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendantsGraphByDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        final Identifier dimensionId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || dimensionId == null || dimensionId.getId() == null || dimensionId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getDescendantsGraphByDimension(identifier.getId(), dist.getId(), dimensionId.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendantsGraphByDimension() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAscendantsGraphByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getAscendantsGraphByIdList(identifier.getIdsList(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getAscendantsGraphByIdList() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDescendantsGraphByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(OrganizationParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.organizationManager.getDescendantsGraphByIdList(identifier.getIdsList(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(OrganizationParams.ORG_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            OrganizationManagerServiceImpl.log.error("Exception in OrganizationManagerServiceImpl - getDescendantsGraphByIdList() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-000", e.getMessage(), (Throwable)e));
        }
    }
    
    protected OrganizationInterceptorService getInterceptor() {
        try {
            final OrganizationInterceptorService service = (OrganizationInterceptorService)this.serviceLocator.getService("OrganizationInterceptorImpl");
            return service;
        }
        catch (Exception ex) {
            OrganizationManagerServiceImpl.log.debug("Interceptor not found", (Throwable)ex);
            return null;
        }
    }
    
    protected OrganizationUnitData fireBeforeCreate(final Request request, final OrganizationUnitData data) {
        OrganizationManagerServiceImpl.log.debug("Invoking before create interceptor");
        final StopWatch sw = new StopWatch();
        sw.start();
        OrganizationUnitData retVal = data;
        try {
            final OrganizationInterceptorService interceptor = this.getInterceptor();
            if (interceptor != null) {
                final Response response = interceptor.beforeCreate(request);
                retVal = (OrganizationUnitData)response.get(OrganizationParams.ORG_DATA.getParamName());
            }
        }
        catch (Exception ex) {
            OrganizationManagerServiceImpl.log.warn("Interceptor invokation threw exception. Proceeding with normal operation.", (Throwable)ex);
        }
        sw.stop();
        OrganizationManagerServiceImpl.log.debug("Create interceptor overhead - " + sw.getElapsedTime() + " ms");
        return retVal;
    }
    
    protected OrganizationUnitData fireBeforeUpdate(final Request request, final OrganizationUnitData data) {
        OrganizationManagerServiceImpl.log.debug("Invoking before update interceptor");
        final StopWatch sw = new StopWatch();
        sw.start();
        OrganizationUnitData retVal = data;
        try {
            final OrganizationInterceptorService interceptor = this.getInterceptor();
            if (interceptor != null) {
                final Response response = interceptor.beforeUpdate(request);
                retVal = (OrganizationUnitData)response.get(OrganizationParams.ORG_DATA.getParamName());
            }
        }
        catch (Exception ex) {
            OrganizationManagerServiceImpl.log.warn("Interceptor invokation threw exception. Proceeding with normal operation.", (Throwable)ex);
        }
        sw.stop();
        OrganizationManagerServiceImpl.log.debug("Update interceptor overhead - " + sw.getElapsedTime() + " ms");
        return retVal;
    }
    
    protected void fireBeforeDelete(final Request request, final Identifier orgId, final BooleanResponse deleteChilds, final BooleanResponse deleteEmployees) {
        OrganizationManagerServiceImpl.log.debug("Invoking before delete interceptor");
        final StopWatch sw = new StopWatch();
        sw.start();
        try {
            final OrganizationInterceptorService interceptor = this.getInterceptor();
            if (interceptor != null) {
                final Response response = interceptor.beforeDelete(request);
                final Identifier orgId2 = (Identifier)response.get(OrganizationParams.ORG_IDENTIFIER.name());
                final BooleanResponse deleteChilds2 = (BooleanResponse)response.get(OrganizationParams.DELETE_CHILDS.name());
                final BooleanResponse deleteEmployees2 = (BooleanResponse)response.get(OrganizationParams.DELETE_EMPLOYEES.name());
                if (orgId2 != null) {
                    orgId.setId(orgId2.getId());
                }
                if (deleteChilds2 != null) {
                    deleteChilds.setResponse(deleteChilds2.isResponse());
                }
                if (deleteEmployees2 != null) {
                    deleteEmployees.setResponse(deleteEmployees2.isResponse());
                }
            }
        }
        catch (Exception ex) {
            OrganizationManagerServiceImpl.log.warn("Interceptor invokation threw exception. Proceeding with normal operation.", (Throwable)ex);
        }
        sw.stop();
        OrganizationManagerServiceImpl.log.debug("Delete interceptor overhead - " + sw.getElapsedTime() + " ms");
    }
    
    protected void fireBeforeSuspend(final Request request, final Identifier orgId, final DateResponse endDate, final BooleanResponse suspendChild, final BooleanResponse suspendEmp) {
        OrganizationManagerServiceImpl.log.debug("Invoking before suspend interceptor");
        final StopWatch sw = new StopWatch();
        sw.start();
        try {
            final OrganizationInterceptorService interceptor = this.getInterceptor();
            if (interceptor != null) {
                final Response response = interceptor.beforeSuspend(request);
                final Identifier orgId2 = (Identifier)response.get(OrganizationParams.ORG_IDENTIFIER.name());
                final DateResponse endDate2 = (DateResponse)response.get(OrganizationParams.END_DATE.name());
                final BooleanResponse suspendChild2 = (BooleanResponse)response.get(OrganizationParams.SUSPEND_CHILDS.name());
                final BooleanResponse suspendEmp2 = (BooleanResponse)response.get(OrganizationParams.SUSPEND_EMPLOYEES.name());
                if (orgId2 != null) {
                    orgId.setId(orgId2.getId());
                }
                if (endDate2 != null) {
                    endDate.setDate(endDate2.getDate());
                }
                if (suspendChild2 != null) {
                    suspendChild.setResponse(suspendChild2.isResponse());
                }
                if (suspendEmp2 != null) {
                    suspendEmp.setResponse(suspendEmp2.isResponse());
                }
            }
        }
        catch (Exception ex) {
            OrganizationManagerServiceImpl.log.warn("Interceptor invokation threw exception. Proceeding with normal operation.", (Throwable)ex);
        }
        sw.stop();
        OrganizationManagerServiceImpl.log.debug("Suspend interceptor overhead - " + sw.getElapsedTime() + " ms");
    }
    
    public Response updateOrganizationUnitInCache(final Request request) {
        final IdentifierList orgIdList = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final Identifier statusIdentifier = (Identifier)request.get(OrganizationParams.STATUS.name());
        if (statusIdentifier == null || statusIdentifier.getId() == null || orgIdList == null || orgIdList.getIdsList() == null || orgIdList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("ORG-200", "No input data in the request"));
        }
        final DateResponse startDateResponse = (DateResponse)request.get(OrganizationParams.ORG_START_DATE.name());
        final DateResponse endDateResponse = (DateResponse)request.get(OrganizationParams.ORG_END_DATE.name());
        final Date startDate = (startDateResponse == null) ? null : startDateResponse.getDate();
        final Date endDate = (endDateResponse == null) ? null : endDateResponse.getDate();
        try {
            final Boolean result = this.organizationManager.updateOrganizationUnitInCache(orgIdList.getIdsList(), statusIdentifier.getId(), startDate, endDate);
            return this.OK(OrganizationParams.UPDATE_STATUS.name(), (BaseValueObject)new BooleanResponse((boolean)result));
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Unknow Exception during cache update"));
        }
    }
    
    public Response reloadCache(final Request request) {
        try {
            this.getOrganizationManager().init();
            return this.OK();
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("ORG-000", "Failed to reload cache", (Throwable)e));
        }
    }
    
    static {
        OrganizationManagerServiceImpl.ORG_MGR_SERVICE = "OrganizationManagerService";
        log = LoggerFactory.getLogger((Class)OrganizationManagerServiceImpl.class);
    }
}

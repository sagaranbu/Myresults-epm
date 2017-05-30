package com.canopus.saas.impl;

import com.canopus.saas.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.saas.domain.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.saas.vo.*;
import com.canopus.saas.vo.params.*;
import com.canopus.dac.*;
import com.canopus.mw.dto.param.*;
import com.kpisoft.org.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.emp.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ TenantBaseManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class TenantBaseManagerServiceImpl extends BaseMiddlewareBean implements TenantBaseManagerService
{
    @Autowired
    private TenantManager manager;
    @Autowired
    private IServiceLocator serviceLocator;
    
    public TenantBaseManagerServiceImpl() {
        this.manager = null;
        this.serviceLocator = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createTenant(Request request) {
        final TenantBaseData data = (TenantBaseData)request.get(TenantParams.TENANT_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        try {
            final Integer tenantId = this.manager.createTenant(data);
            ExecutionContext.getCurrent().getContextValues().put(HeaderParam.TENANT_ID.getParamName(), tenantId);
            final OrganizationManagerService service = (OrganizationManagerService)this.serviceLocator.getService("OrganizationManagerServiceImpl");
            final OrganizationUnitData organizationUnitData = new OrganizationUnitData();
            organizationUnitData.setOrgName(data.getOrgName());
            organizationUnitData.setDescription(data.getOrgName());
            organizationUnitData.setOrgUnitCode("ORG" + tenantId);
            organizationUnitData.setStartDate(new Date());
            request = new Request();
            request.put(OrganizationParams.ORG_DATA.getParamName(), (BaseValueObject)organizationUnitData);
            final Response response = service.createOrganizationUnit(request);
            final Identifier orgId = (Identifier)response.get(OrganizationParams.ORG_IDENTIFIER.getParamName());
            final EmployeeManagerService employeeManagerService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
            final EmployeeData employeeData = new EmployeeData();
            employeeData.setFirstName(data.getFirstName());
            employeeData.setLastName(data.getLastName());
            employeeData.setEmail(data.getEmail());
            employeeData.setEmpCode("EMP" + tenantId);
            employeeData.setEmpType(1);
            final EmployeeOrgRelationshipData empOrgRel = new EmployeeOrgRelationshipData();
            empOrgRel.setOrganizationId(orgId.getId());
            empOrgRel.setHod(false);
            employeeData.getEmpOrgRelData().add(empOrgRel);
            request = new Request();
            request.put(EMPParams.EMP_DATA.name(), (BaseValueObject)employeeData);
            request.put(UMSParams.IS_TENANT_USER.name(), (BaseValueObject)new BooleanResponse(true));
            employeeManagerService.createEmployee(request);
            return this.OK(TenantParams.TENANT_ID.name(), (BaseValueObject)new Identifier(tenantId));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return this.ERROR((Exception)new MiddlewareException("Failed to create Tenant", e.getMessage()));
        }
    }
    
    public Response removeTenant(final Request request) {
        final Identifier id = (Identifier)request.get(TenantParams.TENANT_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        final boolean status = this.manager.removeTenant(id.getId());
        return this.OK(TenantParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
    }
    
    public Response getTenant(final Request request) {
        final Identifier id = (Identifier)request.get(TenantParams.TENANT_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        try {
            final TenantBaseData data = this.manager.getTenant(id.getId());
            return this.OK(TenantParams.TENANT_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new MiddlewareException("Failed to get Tenant", ex.getMessage()));
        }
    }
    
    public Response getAllTenants(final Request request) {
        final List<TenantBaseData> tenantData = this.manager.getAllTenants();
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)tenantData);
        return this.OK(TenantParams.TENANT_TYPE_LIST.name(), (BaseValueObject)list);
    }
    
    public Response updateTenant(final Request request) {
        final TenantBaseData tenantBaseData = (TenantBaseData)request.get(TenantParams.TENANT_DATA.name());
        if (tenantBaseData == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_TENAT_DATA", "No data object in the request"));
        }
        try {
            final Integer id = this.manager.createTenant(tenantBaseData);
            final Identifier identifier = new Identifier();
            identifier.setId(id);
            return this.OK(TenantParams.TENANT_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new MiddlewareException("Failed to update Tenant", ex.getMessage()));
        }
    }
}

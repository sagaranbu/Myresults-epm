package com.kpisoft.user.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.impl.domain.*;
import java.util.*;
import com.kpisoft.user.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ RoleManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class RoleManagerServiceImpl extends BaseMiddlewareBean implements RoleManagerService
{
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public RoleManagerServiceImpl() {
        this.roleManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createRole(final Request request) {
        final RoleData data = (RoleData)request.get(UMSParams.ROLE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Role role = this.getRoleManager().saveOrUpdateRole(data);
            final Identifier id = new Identifier();
            id.setId(role.getRoleDetails().getId());
            return this.OK(UMSParams.ROLE_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - createRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateRole(final Request request) {
        final RoleData data = (RoleData)request.get(UMSParams.ROLE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), " No data object in the request"));
        }
        try {
            final Role role = this.getRoleManager().saveOrUpdateRole(data);
            final Identifier id = new Identifier();
            id.setId(role.getRoleDetails().getId());
            return this.OK(UMSParams.ROLE_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - updateRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_UPDATE_005.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getRole(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.ROLE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Role role = this.getRoleManager().getRole(id.getId());
            final RoleData roleData = role.getRoleDetails();
            return this.OK(UMSParams.ROLE_DATA.name(), (BaseValueObject)roleData);
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - getRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getAllRoles(final Request request) {
        try {
            final List<RoleData> roleList = this.getRoleManager().getAllRoles();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)roleList);
            final Response response = this.OK(UMSParams.ROLE_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - getAllRoles() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response deleteRole(Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.ROLE_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), " No data object in the request"));
        }
        try {
            final Role role = this.getRoleManager().getRole(id.getId());
            final boolean status = role.delete();
            if (status) {
                final List<Integer> idsList = new ArrayList<Integer>();
                idsList.add(id.getId());
                final OrgUserRoleManagerService orgUserRoleService = (OrgUserRoleManagerService)this.serviceLocator.getService("OrgUserRoleManagerServiceImpl");
                final OrgTypeRoleManagerService orgTypeRoleService = (OrgTypeRoleManagerService)this.serviceLocator.getService("OrgTypeRoleManagerServiceImpl");
                request = new Request();
                request.put(OrgUserRoleParams.ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idsList));
                Response response = orgUserRoleService.removeRelationForRoleIds(request);
                response.get(OrgUserRoleParams.STATUS.name());
                request = new Request();
                request.put(OrgTypeRoleParams.ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idsList));
                response = orgTypeRoleService.removeRelationshipByRoleIds(request);
                response.get(OrgTypeRoleParams.STATUS.name());
            }
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - deleteRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response searchRole(final Request request) {
        final RoleData data = (RoleData)request.get(UMSParams.ROLE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), " No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final List<RoleData> result = this.getRoleManager().searchRole(data, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(UMSParams.ROLE_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - searchRole() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public RoleManager getRoleManager() {
        return this.roleManager;
    }
    
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public Response getRolesCount(final Request request) {
        try {
            final Integer rolesCount = this.getRoleManager().getRolesCount();
            return this.OK(UMSParams.ROLES_COUNT.name(), (BaseValueObject)new Identifier(rolesCount));
        }
        catch (Exception ex) {
            RoleManagerServiceImpl.log.error((Object)"Exception in RoleManagerServiceImpl - getRolesCount() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_ROLE_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    static {
        log = Logger.getLogger((Class)RoleManagerServiceImpl.class);
    }
}

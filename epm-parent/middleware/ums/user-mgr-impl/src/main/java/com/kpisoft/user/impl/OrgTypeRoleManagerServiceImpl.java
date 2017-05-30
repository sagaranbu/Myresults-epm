package com.kpisoft.user.impl;

import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.*;
import com.kpisoft.user.impl.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.dac.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OrgTypeRoleManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrgTypeRoleManagerServiceImpl extends BaseMiddlewareBean implements OrgTypeRoleManagerService
{
    @Autowired
    private OrgTypeRoleManager manager;
    private static final Logger log;
    
    public OrgTypeRoleManagerServiceImpl() {
        this.manager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response saveOrgTypeRole(final Request request) {
        final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final int id = this.manager.saveUpdateOrgTypeRole(data);
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleManagerServiceImpl - saveOrgTypeRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateOrgTypeRole(final Request request) {
        final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final int id = this.manager.saveUpdateOrgTypeRole(data);
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleManagerServiceImpl - updateOrgTypeRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrgTypeRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final OrgTypeRoleDomain domain = this.manager.getOrgTypeRole(identifier.getId());
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name(), (BaseValueObject)domain.getRelBean());
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleManagerServiceImpl - getOrgTypeRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response search(final Request request) {
        final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<OrgTypeRoleRelationBean> result = this.manager.search(data);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleManagerServiceImpl - search() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeOrgTypeRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeOrgTypeRole(identifier.getId());
            return this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleManagerServiceImpl - removeOrgTypeRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeRelationshipByRoleIds(final Request request) {
        final IdentifierList roleIds = (IdentifierList)request.get(OrgTypeRoleParams.ROLE_ID_LIST.name());
        if (roleIds == null || roleIds.getIdsList() == null || roleIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeRelationshipByRoleIds(roleIds.getIdsList());
            return this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - removeRelationshipByRoleIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeRelationshipByOrgTypeIds(final Request request) {
        final IdentifierList orgTypeIds = (IdentifierList)request.get(OrgTypeRoleParams.ORGTYPE_ID_LIST.name());
        if (orgTypeIds == null || orgTypeIds.getIdsList() == null || orgTypeIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeRelationshipByOrgTypeIds(orgTypeIds.getIdsList());
            return this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgTypeRoleManagerServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - removeRelationshipByOrgTypeIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrgTypeRoleManagerServiceImpl.class);
    }
}

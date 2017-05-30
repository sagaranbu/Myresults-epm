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
@Remote({ OrgUserRoleManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrgUserRoleManagerServiceImpl extends BaseMiddlewareBean implements OrgUserRoleManagerService
{
    @Autowired
    private OrgUserRoleManager manager;
    private static final Logger log;
    
    public OrgUserRoleManagerServiceImpl() {
        this.manager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response saveOrgUserRole(final Request request) {
        final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)request.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final int id = this.manager.saveUpdateOrgUserRole(data);
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - saveOrgUserRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateOrgUserRole(final Request request) {
        final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)request.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final int id = this.manager.saveUpdateOrgUserRole(data);
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - updateOrgUserRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrgUserRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgUserRoleParams.ORG_USER_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final OrgUserRoleDomain data = this.manager.getOrgUserRole(identifier.getId());
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name(), (BaseValueObject)data.getRelBean());
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - getOrgUserRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response search(final Request request) {
        final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)request.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final List<OrgUserRoleRelationBean> result = this.manager.search(data);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_BEAN_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - search() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeOrgUserRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgUserRoleParams.ORG_USER_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeOrgUserRole(identifier.getId());
            return this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - removeOrgUserRole() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeRelationForUserIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.USER_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeRelationForUserIds(idList.getIdsList());
            return this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - removeRelationForUserIds() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeRelationForRoleIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.ROLE_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeRelationForRoleIds(idList.getIdsList());
            return this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - removeRelationForRoleIds() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeRelationForOrgIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.ORG_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.manager.removeRelationForOrgIds(idList.getIdsList());
            return this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgUserRoleManagerServiceImpl.log.error("Exception in OrgUserRoleManagerServiceImpl - removeRelationForOrgIds() : " + e);
            return this.ERROR((Exception)new MiddlewareException("ORG-USER-ROLE-003", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrgUserRoleManagerServiceImpl.class);
    }
}

package com.kpisoft.user.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.user.dac.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Component
public class OrgUserRoleManager extends BaseDomainManager implements CacheLoader<Integer, OrgUserRoleDomain>
{
    @Autowired
    private OrgUserRoleDataService dataService;
    @Autowired
    @Qualifier("orgUserRoleCache")
    private Cache<Integer, OrgUserRoleDomain> cache;
    
    public OrgUserRoleManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public int saveUpdateOrgUserRole(final OrgUserRoleRelationBean data) {
        int id = 0;
        try {
            final OrgUserRoleDomain domain = new OrgUserRoleDomain(this);
            id = domain.save(data);
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE", e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.cache.remove(id);
            }
        }
        return id;
    }
    
    public OrgUserRoleDomain getOrgUserRole(final Integer id) {
        final OrgUserRoleDomain data = (OrgUserRoleDomain)this.cache.get(id, (CacheLoader)this);
        return data;
    }
    
    public List<OrgUserRoleRelationBean> search(final OrgUserRoleRelationBean data) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name(), (BaseValueObject)data);
        final Response response = this.dataService.search(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN_LIST.name());
        final List<OrgUserRoleRelationBean> result = (List<OrgUserRoleRelationBean>)list.getValueObjectList();
        return result;
    }
    
    public boolean removeOrgUserRole(final Integer id) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ORG_USER_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeOrgUserRole(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgUserRoleParams.STATUS.name());
        if (status.isResponse()) {
            this.cache.remove(id);
        }
        return status.isResponse();
    }
    
    public boolean removeRelationForUserIds(final List<Integer> userIds) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)userIds));
        final Response response = this.dataService.removeRelationForUserIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgUserRoleParams.STATUS.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.cache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public boolean removeRelationForRoleIds(final List<Integer> roleIds) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)roleIds));
        final Response response = this.dataService.removeRelationForRoleIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgUserRoleParams.STATUS.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.cache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public boolean removeRelationForOrgIds(final List<Integer> orgIds) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ORG_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)orgIds));
        final Response response = this.dataService.removeRelationForOrgIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgUserRoleParams.STATUS.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.cache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public OrgUserRoleDomain load(final Integer key) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ORG_USER_ROLE_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getOrgUserRole(request);
        final OrgUserRoleRelationBean bean = (OrgUserRoleRelationBean)response.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        final OrgUserRoleDomain domain = new OrgUserRoleDomain(this);
        domain.setRelBean(bean);
        return domain;
    }
    
    public OrgUserRoleDataService getDataService() {
        return this.dataService;
    }
}

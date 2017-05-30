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
public class OrgTypeRoleManager extends BaseDomainManager implements CacheLoader<Integer, OrgTypeRoleDomain>
{
    @Autowired
    private OrgTypeRoleDataService dataService;
    @Autowired
    @Qualifier("orgTypeRoleCache")
    private Cache<Integer, OrgTypeRoleDomain> cache;
    
    public OrgTypeRoleManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public int saveUpdateOrgTypeRole(final OrgTypeRoleRelationBean relBean) {
        int id = 0;
        try {
            final OrgTypeRoleDomain domain = new OrgTypeRoleDomain(this);
            id = domain.save(relBean);
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
    
    public OrgTypeRoleDomain getOrgTypeRole(final Integer id) {
        final OrgTypeRoleDomain domain = (OrgTypeRoleDomain)this.cache.get(id, (CacheLoader)this);
        return domain;
    }
    
    public List<OrgTypeRoleRelationBean> search(final OrgTypeRoleRelationBean relBean) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name(), (BaseValueObject)relBean);
        final Response response = this.dataService.search(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN_LIST.name());
        final List<OrgTypeRoleRelationBean> result = (List<OrgTypeRoleRelationBean>)list.getValueObjectList();
        return result;
    }
    
    public boolean removeOrgTypeRole(final Integer id) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeOrgTypeRole(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgTypeRoleParams.STATUS.name());
        if (status.isResponse()) {
            this.cache.remove(id);
        }
        return status.isResponse();
    }
    
    public boolean removeRelationshipByRoleIds(final List<Integer> roleIds) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)roleIds));
        final Response response = this.dataService.removeRelationshipByRoleIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgTypeRoleParams.STATUS.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.cache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public boolean removeRelationshipByOrgTypeIds(final List<Integer> orgTypeIds) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ORGTYPE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)orgTypeIds));
        final Response response = this.dataService.removeRelationshipByOrgTypeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgTypeRoleParams.STATUS.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.cache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public OrgTypeRoleDomain load(final Integer key) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getOrgTypeRole(request);
        final OrgTypeRoleRelationBean relBean = (OrgTypeRoleRelationBean)response.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        final OrgTypeRoleDomain domain = new OrgTypeRoleDomain(this);
        domain.setRelBean(relBean);
        return domain;
    }
    
    public OrgTypeRoleDataService getDataService() {
        return this.dataService;
    }
}

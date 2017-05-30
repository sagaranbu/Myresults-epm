package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;

public class OrgTypeRoleDomain extends BaseDomainObject
{
    private OrgTypeRoleManager manager;
    private OrgTypeRoleRelationBean relBean;
    
    public OrgTypeRoleDomain(final OrgTypeRoleManager manager) {
        this.manager = null;
        this.relBean = null;
        this.manager = manager;
    }
    
    public int save(final OrgTypeRoleRelationBean relBean) {
        final Request request = new Request();
        request.put(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name(), (BaseValueObject)relBean);
        final Response response = this.manager.getDataService().saveOrgTypeRole(request);
        final Identifier identifier = (Identifier)response.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name());
        return identifier.getId();
    }
    
    public OrgTypeRoleRelationBean getRelBean() {
        return this.relBean;
    }
    
    public void setRelBean(final OrgTypeRoleRelationBean relBean) {
        this.relBean = relBean;
    }
}

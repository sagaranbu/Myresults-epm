package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;

public class OrgUserRoleDomain extends BaseDomainObject
{
    private OrgUserRoleRelationBean relBean;
    private OrgUserRoleManager manager;
    
    public OrgUserRoleDomain(final OrgUserRoleManager manager) {
        this.relBean = null;
        this.manager = null;
        this.manager = manager;
    }
    
    public int save(final OrgUserRoleRelationBean data) {
        final Request request = new Request();
        request.put(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name(), (BaseValueObject)data);
        final Response response = this.manager.getDataService().saveOrgUserRole(request);
        final Identifier id = (Identifier)response.get(OrgUserRoleParams.ORG_USER_ROLE_ID.name());
        return id.getId();
    }
    
    public OrgUserRoleRelationBean getRelBean() {
        return this.relBean;
    }
    
    public void setRelBean(final OrgUserRoleRelationBean relBean) {
        this.relBean = relBean;
    }
}

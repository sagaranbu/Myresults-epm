package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;

public class OrgTypeRoleRelationBean extends BaseValueObject
{
    private static final long serialVersionUID = -2452295499582168604L;
    private Integer id;
    private Integer orgTypeId;
    private boolean isDeleted;
    private RoleData roleBase;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgTypeId() {
        return this.orgTypeId;
    }
    
    public void setOrgTypeId(final Integer orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public RoleData getRoleBase() {
        return this.roleBase;
    }
    
    public void setRoleBase(final RoleData roleBase) {
        this.roleBase = roleBase;
    }
}

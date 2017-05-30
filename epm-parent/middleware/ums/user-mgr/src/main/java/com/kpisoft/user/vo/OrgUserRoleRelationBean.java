package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;

public class OrgUserRoleRelationBean extends BaseValueObject
{
    private static final long serialVersionUID = -2638888469077103677L;
    private Integer id;
    private Integer orgUnitId;
    private boolean isDeleted;
    private RoleData roleBase;
    private UserData userBase;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
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
    
    public UserData getUserBase() {
        return this.userBase;
    }
    
    public void setUserBase(final UserData userBase) {
        this.userBase = userBase;
    }
}

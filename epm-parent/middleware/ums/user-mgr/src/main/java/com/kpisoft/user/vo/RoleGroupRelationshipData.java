package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class RoleGroupRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = 6059115392432581976L;
    private Integer id;
    @NotNull
    private Integer roleId;
    @NotNull
    private Integer groupId;
    @NotNull
    private Boolean isDeleted;
    
    public RoleGroupRelationshipData() {
        this.isDeleted = false;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
}

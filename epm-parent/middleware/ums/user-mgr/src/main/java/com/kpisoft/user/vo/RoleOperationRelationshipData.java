package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class RoleOperationRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = 5950446303141054750L;
    private Integer id;
    @NotNull
    private Integer roleId;
    @NotNull
    private Integer operationId;
    @NotNull
    private Boolean isDeleted;
    
    public RoleOperationRelationshipData() {
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
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public Integer getOperationId() {
        return this.operationId;
    }
    
    public void setOperationId(final Integer operationId) {
        this.operationId = operationId;
    }
}

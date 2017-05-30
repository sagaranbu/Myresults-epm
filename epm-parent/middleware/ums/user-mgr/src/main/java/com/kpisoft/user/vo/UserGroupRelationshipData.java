package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class UserGroupRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = 6059115392432581976L;
    private Integer id;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer groupId;
    @NotNull
    private Boolean isDeleted;
    
    public UserGroupRelationshipData() {
        this.isDeleted = false;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Integer userId) {
        this.userId = userId;
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
}

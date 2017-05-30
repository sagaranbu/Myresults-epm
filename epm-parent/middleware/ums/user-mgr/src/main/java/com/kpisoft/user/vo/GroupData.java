package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class GroupData extends BaseValueObject
{
    private static final long serialVersionUID = -4980705329223844973L;
    private Integer id;
    @NotNull
    private String name;
    private String description;
    private Integer version;
    private List<RoleGroupRelationshipData> roleGroups;
    private List<UserGroupRelationshipData> userGroups;
    
    public GroupData() {
        this.roleGroups = new ArrayList<RoleGroupRelationshipData>();
        this.userGroups = new ArrayList<UserGroupRelationshipData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public List<RoleGroupRelationshipData> getRoleGroups() {
        return this.roleGroups;
    }
    
    public void setRoleGroups(final List<RoleGroupRelationshipData> roleGroups) {
        this.roleGroups = roleGroups;
    }
    
    public List<UserGroupRelationshipData> getUserGroups() {
        return this.userGroups;
    }
    
    public void setUserGroups(final List<UserGroupRelationshipData> userGroups) {
        this.userGroups = userGroups;
    }
}

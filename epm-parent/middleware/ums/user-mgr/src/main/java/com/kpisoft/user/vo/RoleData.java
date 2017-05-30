package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class RoleData extends BaseValueObject
{
    private static final long serialVersionUID = 346671390587821091L;
    private Integer id;
    @NotNull
    private String name;
    private String description;
    private Integer version;
    private String code;
    private List<RoleOperationRelationshipData> roleOperations;
    
    public RoleData() {
        this.roleOperations = new ArrayList<RoleOperationRelationshipData>();
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
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public List<RoleOperationRelationshipData> getRoleOperations() {
        return this.roleOperations;
    }
    
    public void setRoleOperations(final List<RoleOperationRelationshipData> roleOperations) {
        this.roleOperations = roleOperations;
    }
}

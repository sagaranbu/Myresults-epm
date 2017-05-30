package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.*;
import java.util.*;

@Audited
@Entity
@Table(name = "COR_MAS_ROLE")
@SQLDelete(sql = "UPDATE COR_MAS_ROLE SET IS_DELETED = 1 WHERE CMR_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Role extends BaseTenantEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ROLE_ID_SEQ")
    @SequenceGenerator(name = "ROLE_ID_SEQ", sequenceName = "ROLE_ID_SEQ")
    @Column(name = "CMR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "CODE", length = 127)
    private String code;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "roleId")
    @AuditMappedBy(mappedBy = "roleId")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<RoleOperationRelationship> roleOperations;
    @Column(name = "IS_DELETED", length = 1)
    private boolean deleted;
    
    public Role() {
        this.roleOperations = new ArrayList<RoleOperationRelationship>();
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public List<RoleOperationRelationship> getRoleOperations() {
        return Collections.unmodifiableList((List<? extends RoleOperationRelationship>)this.roleOperations);
    }
    
    public void setRoleOperations(final List<RoleOperationRelationship> roleOperations) {
        this.roleOperations.clear();
        if (roleOperations != null && !roleOperations.isEmpty()) {
            for (final RoleOperationRelationship iterator : roleOperations) {
                this.roleOperations.add(iterator);
            }
        }
    }
}

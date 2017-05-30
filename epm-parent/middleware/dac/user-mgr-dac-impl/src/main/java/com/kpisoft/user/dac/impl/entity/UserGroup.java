package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.CascadeType;

public class UserGroup extends BaseTenantEntity
{
    private static final long serialVersionUID = -7330471167853822205L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USR_GRP_ID_SEQ")
    @SequenceGenerator(name = "USR_GRP_ID_SEQ", sequenceName = "USR_GRP_ID_SEQ")
    @Column(name = "UMG_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "IS_DELETED", length = 1)
    private boolean deleted;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "UMG_PK_ID")
    @AuditMappedBy(mappedBy = "groupId")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<RoleGroupRelationship> roleGroups;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "UMG_PK_ID")
    @AuditMappedBy(mappedBy = "groupId")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<UserGroupRelationship> userGroups;
    
    public UserGroup() {
        this.roleGroups = new ArrayList<RoleGroupRelationship>();
        this.userGroups = new ArrayList<UserGroupRelationship>();
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
    
    public List<RoleGroupRelationship> getRoleGroups() {
        return Collections.unmodifiableList((List<? extends RoleGroupRelationship>)this.roleGroups);
    }
    
    public void setRoleGroups(final List<RoleGroupRelationship> roleGroups) {
        this.roleGroups.clear();
        if (roleGroups != null && !roleGroups.isEmpty()) {
            for (final RoleGroupRelationship iterator : roleGroups) {
                this.roleGroups.add(iterator);
            }
        }
    }
    
    public List<UserGroupRelationship> getUserGroups() {
        return Collections.unmodifiableList((List<? extends UserGroupRelationship>)this.userGroups);
    }
    
    public void setUserGroups(final List<UserGroupRelationship> userGroups) {
        this.userGroups.clear();
        if (userGroups != null && !userGroups.isEmpty()) {
            for (final UserGroupRelationship iterator : userGroups) {
                this.userGroups.add(iterator);
            }
        }
    }
}

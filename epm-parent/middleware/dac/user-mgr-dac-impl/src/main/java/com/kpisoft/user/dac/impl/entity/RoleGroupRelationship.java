package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;

public class RoleGroupRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -2310584839697741552L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ROL_GRP_ID_SEQ")
    @SequenceGenerator(name = "ROL_GRP_ID_SEQ", sequenceName = "ROL_GRP_ID_SEQ")
    @Column(name = "UMRG_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CMR_FK_ID", length = 11)
    private Integer roleId;
    @Column(name = "UMG_PK_ID", length = 11)
    private Integer groupId;
    @Column(name = "IS_DELETED", length = 1)
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

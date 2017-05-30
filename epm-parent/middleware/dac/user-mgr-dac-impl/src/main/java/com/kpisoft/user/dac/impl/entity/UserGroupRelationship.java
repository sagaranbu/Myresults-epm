package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;

public class UserGroupRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -2310584839697741552L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USR_GRP_ID_SEQ")
    @SequenceGenerator(name = "USR_GRP_ID_SEQ", sequenceName = "USR_GRP_ID_SEQ")
    @Column(name = "UMUG_PK_ID", length = 11)
    private Integer id;
    @Column(name = "UDU_PK_ID", length = 11)
    private Integer userId;
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
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Integer userId) {
        this.userId = userId;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
}

package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "USR_MAP_USER_ROLE")
@SQLDelete(sql = "UPDATE USR_MAP_USER_ROLE SET IS_DELETED = 1 WHERE UMUR_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class UserRoleRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -2310584839697741552L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USR_ROL_ID_SEQ")
    @SequenceGenerator(name = "USR_ROL_ID_SEQ", sequenceName = "USR_ROL_ID_SEQ")
    @Column(name = "UMUR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "UDU_PK_ID", length = 11)
    private Integer userId;
    @Column(name = "CMR_FK_ID", length = 11)
    private Integer roleId;
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
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
}

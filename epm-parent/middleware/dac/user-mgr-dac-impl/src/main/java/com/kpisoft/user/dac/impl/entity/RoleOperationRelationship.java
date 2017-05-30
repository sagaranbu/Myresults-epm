package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "COR_MAP_ROLE_OPERATION")
@SQLDelete(sql = "UPDATE COR_MAP_ROLE_OPERATION SET IS_DELETED = 1 WHERE CMRO_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class RoleOperationRelationship extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -2310584839697741552L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ROL_OPR_ID_SEQ")
    @SequenceGenerator(name = "ROL_OPR_ID_SEQ", sequenceName = "ROL_OPR_ID_SEQ")
    @Column(name = "CMRO_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CMR_FK_ID", length = 11)
    private Integer roleId;
    @Column(name = "CMO_FK_ID", length = 11)
    private Integer operationId;
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

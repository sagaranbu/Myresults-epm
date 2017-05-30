package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;
import org.hibernate.envers.*;

public class OrgUserRoleRelation extends BaseTenantEntity
{
    private static final long serialVersionUID = 8754276123203957817L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USR_UOR_ID_SEQ")
    @SequenceGenerator(name = "USR_UOR_ID_SEQ", sequenceName = "USR_UOR_ID_SEQ")
    @Column(name = "USR_UOR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ODO_FK_ID")
    private Integer orgUnitId;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "CMR_FK_ID", referencedColumnName = "CMR_PK_ID")
    @NotAudited
    private RoleBase roleBase;
    @ManyToOne
    @JoinColumn(name = "UDU_FK_ID", referencedColumnName = "UDU_PK_ID")
    @NotAudited
    private UserBase userBase;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public RoleBase getRoleBase() {
        return this.roleBase;
    }
    
    public void setRoleBase(final RoleBase roleBase) {
        this.roleBase = roleBase;
    }
    
    public UserBase getUserBase() {
        return this.userBase;
    }
    
    public void setUserBase(final UserBase userBase) {
        this.userBase = userBase;
    }
}

package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;
import org.hibernate.envers.*;

public class OrgTypeRoleRelation extends BaseTenantEntity
{
    private static final long serialVersionUID = -3377169536907907467L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USR_OTR_ID_SEQ")
    @SequenceGenerator(name = "USR_OTR_ID_SEQ", sequenceName = "USR_OTR_ID_SEQ")
    @Column(name = "USR_OTR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "OMS_FK_ID")
    private Integer orgTypeId;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "CMR_FK_ID", referencedColumnName = "CMR_PK_ID")
    @NotAudited
    private RoleBase roleBase;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgTypeId() {
        return this.orgTypeId;
    }
    
    public void setOrgTypeId(final Integer orgTypeId) {
        this.orgTypeId = orgTypeId;
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
}

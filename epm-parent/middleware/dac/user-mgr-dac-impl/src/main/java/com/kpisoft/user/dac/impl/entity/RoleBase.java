package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Immutable
@Entity
@Table(name = "COR_MAS_ROLE")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class RoleBase extends BaseTenantEntity
{
    private static final long serialVersionUID = 2174628319154124354L;
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
    @Column(name = "IS_DELETED", length = 1)
    private boolean deleted;
    
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

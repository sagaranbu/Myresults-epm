package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Deprecated
@Audited
@Entity
@Immutable
@Table(name = "ORG_MET_POSITION")
public class PositionIdentity extends BaseTenantEntity
{
    private static final long serialVersionUID = -1734742746673704556L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "POSITION_ID_SEQ")
    @SequenceGenerator(name = "POSITION_ID_SEQ", sequenceName = "POSITION_ID_SEQ")
    @Column(name = "OMP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "POSITION_NAME", length = 45)
    private String name;
    @Column(name = "POSITION_CODE", length = 45, nullable = false)
    private String positionCode;
    
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
    
    public String getPositionCode() {
        return this.positionCode;
    }
    
    public void setPositionCode(final String positionCode) {
        this.positionCode = positionCode;
    }
}

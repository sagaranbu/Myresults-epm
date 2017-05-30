package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "PROGRAM_ZONE_MAPPING")
@SQLDelete(sql = "UPDATE PROGRAM_ZONE_MAPPING SET IS_DELETED = 1 WHERE PZP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ProgramZoneMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = -670569946222976196L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PZP_ID_SEQ")
    @SequenceGenerator(name = "PZP_ID_SEQ", sequenceName = "PZP_ID_SEQ")
    @Column(name = "PZP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SDP_FK_ID", length = 11)
    private Integer programId;
    @Column(name = "ODO_PK_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "VALUE", length = 127)
    private String value;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

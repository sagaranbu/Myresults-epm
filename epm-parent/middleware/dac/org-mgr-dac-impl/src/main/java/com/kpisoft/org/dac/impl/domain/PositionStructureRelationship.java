package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Deprecated
@Audited
@Entity
@Table(name = "ORG_MET_POS_DIM")
@SQLDelete(sql = "UPDATE ORG_MET_POS_DIM SET IS_DELETED = 1 WHERE OMPD_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PositionStructureRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 4487282725193182585L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "POS_DIM_ID_SEQ")
    @SequenceGenerator(name = "POS_DIM_ID_SEQ", sequenceName = "POS_DIM_ID_SEQ")
    @Column(name = "OMPD_PK_ID", length = 11)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "OMP_FK_ID", referencedColumnName = "OMP_PK_ID", nullable = false)
    private Position position;
    @Column(name = "OMS_FK_ID")
    private Integer organizationStructure;
    @Column(name = "START_DATE", length = 45, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public void setPosition(final Position position) {
        this.position = position;
    }
    
    public Integer getOrganizationStructure() {
        return this.organizationStructure;
    }
    
    public void setOrganizationStructure(final Integer organizationStructure) {
        this.organizationStructure = organizationStructure;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
}

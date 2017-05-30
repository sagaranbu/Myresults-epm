package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;

import org.hibernate.envers.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Deprecated
@Audited
@Entity
@Table(name = "ORG_MET_POSITION", uniqueConstraints = { @UniqueConstraint(columnNames = { "POSITION_CODE", "TENANT_ID" }) })
@SQLDelete(sql = "UPDATE ORG_MET_POSITION SET IS_DELETED = 1 WHERE OMP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Position extends BaseTenantEntity
{
    private static final long serialVersionUID = 2202825425313779156L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "POSITION_ID_SEQ")
    @SequenceGenerator(name = "POSITION_ID_SEQ", sequenceName = "POSITION_ID_SEQ")
    @Column(name = "OMP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "POSITION_MASTER_ID", length = 11)
    private Integer positionMasterId;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "AT_LEVEL", length = 11)
    private Integer level;
    @Column(name = "SOURCE_ID", length = 11)
    private Integer sourceId;
    @Column(name = "ENTITY_ID", length = 11)
    private Integer entityId;
    @Column(name = "POSITION_NAME", length = 45, nullable = false)
    private String name;
    @Column(name = "POSITION_CODE", length = 45, nullable = false)
    private String positionCode;
    @Column(name = "START_DATE", length = 45, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "status", length = 11)
    private Integer status;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "position")
    @AuditMappedBy(mappedBy = "position")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<PositionStructureRelationship> posStrData;
    
    public Position() {
        this.startDate = new Date();
        this.status = 1;
        this.posStrData = new ArrayList<PositionStructureRelationship>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getPositionMasterId() {
        return this.positionMasterId;
    }
    
    public void setPositionMasterId(final Integer positionMasterId) {
        this.positionMasterId = positionMasterId;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final Integer sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
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
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public String getPositionCode() {
        return this.positionCode;
    }
    
    public void setPositionCode(final String positionCode) {
        this.positionCode = positionCode;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public List<PositionStructureRelationship> getPosStrData() {
        return this.posStrData;
    }
    
    public void setPosStrData(final List<PositionStructureRelationship> posStrData) {
        this.posStrData = posStrData;
    }
}

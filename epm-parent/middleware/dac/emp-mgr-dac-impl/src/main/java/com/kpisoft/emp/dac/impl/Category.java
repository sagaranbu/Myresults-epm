package com.kpisoft.emp.dac.impl;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "EMP_MAS_EMP_CATEGORY")
@SQLDelete(sql = "UPDATE EMP_MAS_EMP_CATEGORY SET IS_DELETED = 1 WHERE EME_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Category extends BaseTenantEntity
{
    private static final long serialVersionUID = 1088950737019016166L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_CATEGORY_ID_SEQ")
    @SequenceGenerator(name = "EMP_CATEGORY_ID_SEQ", sequenceName = "EMP_CATEGORY_ID_SEQ")
    @Column(name = "EME_PK_ID", length = 11)
    private Integer id;
    @Column(name = "TYPE", length = 255)
    private String type;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "SOURCE_ID", length = 11)
    private Integer sourceId;
    @Column(name = "LEVEL_NUMBER", length = 11)
    private Integer level;
    @Column(name = "ENTITY_ID", length = 11)
    private Integer entityId;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
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
    
    public Integer getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final Integer sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

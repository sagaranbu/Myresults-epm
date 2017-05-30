package com.kpisoft.emp.dac.impl;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EMP_DET_DESG", uniqueConstraints = { @UniqueConstraint(columnNames = { "EDD_CODE", "TENANT_ID" }) })
@SQLDelete(sql = "UPDATE EMP_DET_DESG SET IS_DELETED = 1 WHERE EDD_PK_DESG_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Designation extends BaseTenantEntity
{
    private static final long serialVersionUID = 1777697164733605939L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DESG_ID_SEQ")
    @SequenceGenerator(name = "DESG_ID_SEQ", sequenceName = "DESG_ID_SEQ")
    @Column(name = "EDD_PK_DESG_ID", length = 11)
    private Integer id;
    @Column(name = "EDD_NAME", length = 255)
    private String name;
    @Column(name = "EDD_CODE", length = 255)
    private String code;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "START_DATE", length = 45)
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
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
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
}

package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_MET_FIELD_DATA")
@SQLDelete(sql = "UPDATE ORG_MET_FIELD_DATA SET IS_DELETED = 1 WHERE OMF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrganizationFieldData extends BaseTenantEntity
{
    private static final long serialVersionUID = -7684453575044126721L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_FIELD_ID_SEQ")
    @SequenceGenerator(name = "ORG_FIELD_ID_SEQ", sequenceName = "ORG_FIELD_ID_SEQ")
    @Column(name = "OMF_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CMF_FK_ID", length = 11)
    private Integer fieldId;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "ODO_FK_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "DATA", length = 4000)
    private String data;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getFieldId() {
        return this.fieldId;
    }
    
    public void setFieldId(final Integer fieldId) {
        this.fieldId = fieldId;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

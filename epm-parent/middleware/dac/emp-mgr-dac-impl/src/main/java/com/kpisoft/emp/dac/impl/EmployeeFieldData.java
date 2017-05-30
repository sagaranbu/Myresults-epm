package com.kpisoft.emp.dac.impl;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import org.hibernate.envers.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EMP_MET_FIELD_DATA")
@SQLDelete(sql = "UPDATE EMP_MET_FIELD_DATA SET IS_DELETED = 1 WHERE EMF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
@Audited
public class EmployeeFieldData extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -599811011016076900L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_FIELD_ID_SEQ")
    @SequenceGenerator(name = "EMP_FIELD_ID_SEQ", sequenceName = "EMP_FIELD_ID_SEQ")
    @Column(name = "EMF_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CMF_FK_ID", length = 11)
    private Integer fieldId;
    @Column(name = "DATA", length = 4000)
    private String data;
    @Column(name = "EDE_FK_EMPLOYEE_ID", length = 11)
    private Integer employeeId;
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
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}

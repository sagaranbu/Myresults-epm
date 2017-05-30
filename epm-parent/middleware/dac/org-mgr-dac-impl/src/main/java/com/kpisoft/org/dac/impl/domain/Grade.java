package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "EMP_MAS_GRADE", uniqueConstraints = { @UniqueConstraint(columnNames = { "GRADE_CODE", "TENANT_ID" }) })
@SQLDelete(sql = "UPDATE EMP_MAS_GRADE SET IS_DELETED = 1 WHERE EMG_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Grade extends BaseTenantEntity
{
    private static final long serialVersionUID = -5451002382890378406L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GRD_ID_SEQ")
    @SequenceGenerator(name = "GRD_ID_SEQ", sequenceName = "GRD_ID_SEQ")
    @Column(name = "EMG_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 45)
    private String name;
    @Column(name = "LEVEL_NUM", length = 45)
    private Integer level;
    @Column(name = "CATEGORY", length = 45)
    private String category;
    @Column(name = "GRADE_CODE", length = 45)
    private String gradeCode;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    
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
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public String getGradeCode() {
        return this.gradeCode;
    }
    
    public void setGradeCode(final String gradeCode) {
        this.gradeCode = gradeCode;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

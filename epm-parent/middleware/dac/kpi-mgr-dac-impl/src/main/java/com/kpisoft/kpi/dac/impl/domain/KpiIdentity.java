package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "KPI_DET_BASE")
public class KpiIdentity extends BaseTenantEntity
{
    private static final long serialVersionUID = 7394638277776428843L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KPI_ID_SEQ")
    @SequenceGenerator(name = "KPI_ID_SEQ", sequenceName = "KPI_ID_SEQ", allocationSize = 100)
    @Column(name = "KDB_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 45, nullable = false)
    private String name;
    @Column(name = "WEIGHTAGE", length = 11, nullable = false)
    private Float weightage;
    @Column(name = "CODE", length = 45, nullable = false)
    private String code;
    
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
    
    public Float getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Float weightage) {
        this.weightage = weightage;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
}

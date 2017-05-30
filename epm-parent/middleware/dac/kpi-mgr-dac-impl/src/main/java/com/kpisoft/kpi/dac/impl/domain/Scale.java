package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.*;

@Entity
@Table(name = "KPI_MET_SCALE")
@SQLDelete(sql = "UPDATE KPI_MET_SCALE SET IS_DELETED = 1 WHERE KMS_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Scale extends BaseTenantEntity
{
    private static final long serialVersionUID = -2947210739990440932L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCALE_ID_SEQ")
    @SequenceGenerator(name = "SCALE_ID_SEQ", sequenceName = "SCALE_ID_SEQ", allocationSize = 100)
    @Column(name = "KMS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CODE", length = 45)
    private String code;
    @Column(name = "LEVEL_NUM", length = 11)
    private Integer level;
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "scale")
    private List<ScaleValue> alkpiMasterScaleValue;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Scale() {
        this.alkpiMasterScaleValue = new ArrayList<ScaleValue>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public List<ScaleValue> getAlkpiMasterScaleValue() {
        return this.alkpiMasterScaleValue;
    }
    
    public void setAlkpiMasterScaleValue(final List<ScaleValue> alkpiMasterScaleValue) {
        this.alkpiMasterScaleValue.clear();
        if (alkpiMasterScaleValue != null && !alkpiMasterScaleValue.isEmpty()) {
            for (final ScaleValue iterator : alkpiMasterScaleValue) {
                this.alkpiMasterScaleValue.add(iterator);
                iterator.setScale(this);
            }
        }
    }
}

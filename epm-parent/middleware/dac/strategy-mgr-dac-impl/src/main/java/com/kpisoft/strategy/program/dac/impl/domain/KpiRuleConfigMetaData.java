package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.canopus.entity.domain.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "STR_RUL_KPI_CONFIG")
@SQLDelete(sql = "UPDATE STR_RUL_KPI_CONFIG SET IS_DELETED = 1 WHERE KRC_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class KpiRuleConfigMetaData extends BaseTenantEntity
{
    private static final long serialVersionUID = 3527247148975869867L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_RULE_SEQ")
    @SequenceGenerator(name = "KPI_RULE_SEQ", sequenceName = "KPI_RULE_SEQ")
    @Column(name = "KRC_PK_ID", length = 11)
    private Integer id;
    @Column(name = "VALUED", length = 127)
    private String value;
    @ManyToOne
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    private SystemMasterBaseMetaData systemMasterBaseData;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "SRP_FK_ID", length = 11)
    private Integer programPolicyId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public SystemMasterBaseMetaData getSystemMasterBaseData() {
        return this.systemMasterBaseData;
    }
    
    public void setSystemMasterBaseData(final SystemMasterBaseMetaData systemMasterBaseData) {
        this.systemMasterBaseData = systemMasterBaseData;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getProgramPolicyId() {
        return this.programPolicyId;
    }
    
    public void setProgramPolicyId(final Integer programPolicyId) {
        this.programPolicyId = programPolicyId;
    }
}

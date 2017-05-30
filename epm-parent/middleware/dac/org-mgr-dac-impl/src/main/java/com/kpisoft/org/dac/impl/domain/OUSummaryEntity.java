package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "ORG_SUMMARY_OUSUMMARY")
public class OUSummaryEntity extends BaseTenantEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OUSUMMARY_PK_ID_SEQ")
    @SequenceGenerator(name = "OUSUMMARY_PK_ID_SEQ", sequenceName = "OUSUMMARY_PK_ID_SEQ")
    @Column(name = "OUSUMMARY_PK_ID", length = 11)
    int id;
    @Column(name = "LOCAL_EMP_COUNT")
    int localEmpCount;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public int getLocalEmpCount() {
        return this.localEmpCount;
    }
    
    public void setLocalEmpCount(final int localEmpCount) {
        this.localEmpCount = localEmpCount;
    }
}

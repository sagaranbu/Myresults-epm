package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAP_OPERATION_HIERARCHY")
public class OperationHierarchy extends BaseTenantEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OPER_HRCHY_ID_SEQ")
    @SequenceGenerator(name = "OPER_HRCHY_ID_SEQ", sequenceName = "OPER_HRCHY_ID_SEQ")
    @Column(name = "SOH_PK_ID", length = 11)
    private Integer id;
    @Column(name = "PARENT_OPERATION_ID", length = 11)
    private Integer parentOperId;
    @Column(name = "CHILD_OPERATION_ID", length = 11)
    private Integer childOperId;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getParentOperId() {
        return this.parentOperId;
    }
    
    public void setParentOperId(final Integer parentOperId) {
        this.parentOperId = parentOperId;
    }
    
    public Integer getChildOperId() {
        return this.childOperId;
    }
    
    public void setChildOperId(final Integer childOperId) {
        this.childOperId = childOperId;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
}

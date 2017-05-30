package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MAP_STRATEGY_TREE")
@SQLDelete(sql = "UPDATE COR_MAP_STRATEGY_TREE SET IS_DELETED = 1 WHERE STR_TN_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyNodeTemplate extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -7230646119673981750L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_TN_ID_SEQ")
    @SequenceGenerator(name = "STR_TN_ID_SEQ", sequenceName = "STR_TN_ID_SEQ")
    @Column(name = "STR_TN_PK_ID", length = 11)
    private Integer id;
    @Column(name = "DISPLAY_ORDER", length = 11)
    private Integer displayOrder;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "STRL_FK_ID", length = 11)
    private Integer strTmpLvlId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }
    
    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getStrTmpLvlId() {
        return this.strTmpLvlId;
    }
    
    public void setStrTmpLvlId(final Integer strTmpLvlId) {
        this.strTmpLvlId = strTmpLvlId;
    }
}

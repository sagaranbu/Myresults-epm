package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;


@Audited
@Entity
@Table(name = "STR_MAS_STRATEGY_LEVEL")
@SQLDelete(sql = "UPDATE STR_MAS_STRATEGY_LEVEL SET IS_DELETED = 1 WHERE STR_LVL_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyLevel extends BaseTenantEntity
{
    private static final long serialVersionUID = 9119998603278727220L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_LVL_ID_SEQ")
    @SequenceGenerator(name = "STR_LVL_ID_SEQ", sequenceName = "STR_LVL_ID_SEQ")
    @Column(name = "STR_LVL_PK_ID", length = 11)
    private Integer id;
    @Column(name = "LEVEL_NUM", length = 11)
    private Integer level;
    @Column(name = "MIN_COUNT", length = 11)
    private Integer minCount;
    @Column(name = "MAX_COUNT", length = 11)
    private Integer maxCount;
    @Column(name = "NAME", length = 64)
    private String name;
    @Column(name = "DESCRIPTION", length = 100)
    private String description;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "STR_FK_ID", length = 11)
    private Integer strId;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "STR_LVL_FK_ID", referencedColumnName = "STR_LVL_PK_ID")
    private List<StrategyNode> strategyNode;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getMinCount() {
        return this.minCount;
    }
    
    public void setMinCount(final Integer minCount) {
        this.minCount = minCount;
    }
    
    public Integer getMaxCount() {
        return this.maxCount;
    }
    
    public void setMaxCount(final Integer maxCount) {
        this.maxCount = maxCount;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getStrId() {
        return this.strId;
    }
    
    public void setStrId(final Integer strId) {
        this.strId = strId;
    }
    
    public List<StrategyNode> getStrategyNode() {
        return this.strategyNode;
    }
    
    public void setStrategyNode(final List<StrategyNode> strategyNode) {
        this.strategyNode = strategyNode;
    }
}

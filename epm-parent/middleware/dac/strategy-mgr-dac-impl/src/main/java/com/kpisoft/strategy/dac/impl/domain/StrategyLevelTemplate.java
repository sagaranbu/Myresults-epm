package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MAS_STRATEGY_LEVEL")
@SQLDelete(sql = "UPDATE COR_MAS_STRATEGY_LEVEL SET IS_DELETED = 1 WHERE STRL_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyLevelTemplate extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 1360079586967186248L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_LVL_TEMP_ID_SEQ")
    @SequenceGenerator(name = "STR_LVL_TEMP_ID_SEQ", sequenceName = "STR_LVL_TEMP_ID_SEQ")
    @Column(name = "STRL_PK_ID", length = 11)
    private Integer id;
    @Column(name = "LEVEL_NUM", length = 11)
    private Integer level;
    @Column(name = "MIN_COUNT", length = 11)
    private Integer minCount;
    @Column(name = "MAX_COUNT", length = 11)
    private Integer maxCount;
    @Column(name = "NAME", length = 64)
    private String name;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "DESCRIPTION", length = 100)
    private String description;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "STR_TEMP_FK_ID", length = 11)
    private Integer strTempId;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "STRL_FK_ID", referencedColumnName = "STRL_PK_ID")
    private List<StrategyNodeTemplate> strategyNodeTemplate;
    
    public StrategyLevelTemplate() {
        this.strategyNodeTemplate = new ArrayList<StrategyNodeTemplate>();
    }
    
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
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
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
    
    public Integer getStrTempId() {
        return this.strTempId;
    }
    
    public void setStrTempId(final Integer strTempId) {
        this.strTempId = strTempId;
    }
    
    public List<StrategyNodeTemplate> getStrategyNodeTemplate() {
        return this.strategyNodeTemplate;
    }
    
    public void setStrategyNodeTemplate(final List<StrategyNodeTemplate> strategyNodeTemplate) {
        this.strategyNodeTemplate = strategyNodeTemplate;
    }
}

package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.canopus.entity.domain.*;
import javax.persistence.*;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_DET_STRATEGY_TEMPLATE")
@SQLDelete(sql = "UPDATE COR_DET_STRATEGY_TEMPLATE SET IS_DELETED = 1 WHERE STR_TEMP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyTemplate extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -7317404995690672043L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_TEMP_ID_SEQ")
    @SequenceGenerator(name = "STR_TEMP_ID_SEQ", sequenceName = "STR_TEMP_ID_SEQ")
    @Column(name = "STR_TEMP_PK_ID", length = 11)
    private Integer id;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @ManyToOne
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    private SystemMasterBaseMetaData systemMasterBaseData;
    @Column(name = "TYPE", length = 11)
    private Integer type;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "GOAL", length = 127)
    private String goal;
    @Column(name = "VISION", length = 127)
    private String vision;
    @Column(name = "MISSION", length = 127)
    private String mission;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "STR_TEMP_FK_ID", referencedColumnName = "STR_TEMP_PK_ID")
    private List<StrategyLevelTemplate> strategyLevelTemplate;
    
    public StrategyTemplate() {
        this.strategyLevelTemplate = new ArrayList<StrategyLevelTemplate>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public SystemMasterBaseMetaData getSystemMasterBaseData() {
        return this.systemMasterBaseData;
    }
    
    public void setSystemMasterBaseData(final SystemMasterBaseMetaData systemMasterBaseData) {
        this.systemMasterBaseData = systemMasterBaseData;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public String getGoal() {
        return this.goal;
    }
    
    public void setGoal(final String goal) {
        this.goal = goal;
    }
    
    public String getVision() {
        return this.vision;
    }
    
    public void setVision(final String vision) {
        this.vision = vision;
    }
    
    public String getMission() {
        return this.mission;
    }
    
    public void setMission(final String mission) {
        this.mission = mission;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public List<StrategyLevelTemplate> getStrategyLevelTemplate() {
        return this.strategyLevelTemplate;
    }
    
    public void setStrategyLevelTemplate(final List<StrategyLevelTemplate> strategyLevelTemplate) {
        this.strategyLevelTemplate = strategyLevelTemplate;
    }
}

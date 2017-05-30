package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import com.canopus.entity.domain.*;
import org.hibernate.envers.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "STR_DET_STRATEGY")
@SQLDelete(sql = "UPDATE STR_DET_STRATEGY SET IS_DELETED = 1 WHERE STR_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Strategy extends BaseTenantEntity
{
    private static final long serialVersionUID = 2823874206634562664L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_ID_SEQ")
    @SequenceGenerator(name = "STR_ID_SEQ", sequenceName = "STR_ID_SEQ")
    @Column(name = "STR_PK_ID", length = 11)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private SystemMasterBaseMetaData systemMasterBaseData;
    @Column(name = "TYPE", length = 11)
    private Integer type;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "GOAL", length = 127)
    private String goal;
    @Column(name = "OBJECTIVE", length = 127)
    private String objective;
    @Column(name = "MISSION", length = 127)
    private String mission;
    @Column(name = "IS_TEMPLATE", length = 11)
    private Integer isTemplate;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "STR_FK_ID", referencedColumnName = "STR_PK_ID")
    private List<StrategyLevel> strategyLevel;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
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
    
    public String getGoal() {
        return this.goal;
    }
    
    public void setGoal(final String goal) {
        this.goal = goal;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    public void setObjective(final String objective) {
        this.objective = objective;
    }
    
    public String getMission() {
        return this.mission;
    }
    
    public void setMission(final String mission) {
        this.mission = mission;
    }
    
    public Integer getIsTemplate() {
        return this.isTemplate;
    }
    
    public void setIsTemplate(final Integer isTemplate) {
        this.isTemplate = isTemplate;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public List<StrategyLevel> getStrategyLevel() {
        return this.strategyLevel;
    }
    
    public void setStrategyLevel(final List<StrategyLevel> strategyLevel) {
        this.strategyLevel = strategyLevel;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}

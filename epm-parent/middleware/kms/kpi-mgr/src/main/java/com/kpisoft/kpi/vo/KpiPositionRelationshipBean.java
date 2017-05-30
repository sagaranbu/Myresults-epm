package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;

public class KpiPositionRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = -3114524170579944383L;
    private Integer id;
    private Integer relationshipBaseId;
    private Integer positionId;
    private Integer kpiId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getRelationshipBaseId() {
        return this.relationshipBaseId;
    }
    
    public void setRelationshipBaseId(final Integer relationshipBaseId) {
        this.relationshipBaseId = relationshipBaseId;
    }
    
    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
}

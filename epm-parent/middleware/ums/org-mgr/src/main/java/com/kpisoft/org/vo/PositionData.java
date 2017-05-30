package com.kpisoft.org.vo;

import javax.validation.constraints.*;
import java.util.*;

@Deprecated
public class PositionData extends PositionIdentityBean
{
    private static final long serialVersionUID = 7096459027890653108L;
    private Integer positionMasterId;
    private Integer parentId;
    private Integer level;
    private Integer sourceId;
    private Integer entityId;
    private Integer status;
    private List<PositionStructureRelationshipData> posStrData;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public PositionData() {
        this.status = 1;
        this.posStrData = new ArrayList<PositionStructureRelationshipData>();
        this.startDate = new Date();
    }
    
    public Integer getPositionMasterId() {
        return this.positionMasterId;
    }
    
    public void setPositionMasterId(final Integer positionMasterId) {
        this.positionMasterId = positionMasterId;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final Integer sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getEntityId() {
        return this.entityId;
    }
    
    public void setEntityId(final Integer entityId) {
        this.entityId = entityId;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
    
    public List<PositionStructureRelationshipData> getPosStrData() {
        return this.posStrData;
    }
    
    public void setPosStrData(final List<PositionStructureRelationshipData> posStrData) {
        this.posStrData = posStrData;
    }
}

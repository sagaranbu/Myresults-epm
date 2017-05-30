package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import java.util.*;

@Deprecated
public class PositionStructureRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = 5934165473548040932L;
    private Integer id;
    private Integer positionId;
    private Integer organizationStructure;
    private Date startDate;
    private Date endDate;
    
    public PositionStructureRelationshipData() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }
    
    public Integer getOrganizationStructure() {
        return this.organizationStructure;
    }
    
    public void setOrganizationStructure(final Integer organizationStructure) {
        this.organizationStructure = organizationStructure;
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
}

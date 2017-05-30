package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

@Deprecated
public class OrganizationRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = -2638872165146107059L;
    private Integer id;
    @NotNull
    private Integer sourceId;
    @NotNull
    private Integer destinationId;
    private Integer relationshipType;
    @NotNull
    private Integer dimension;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public OrganizationRelationshipData() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final Integer sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getDestinationId() {
        return this.destinationId;
    }
    
    public void setDestinationId(final Integer destinationId) {
        this.destinationId = destinationId;
    }
    
    public Integer getRelationshipType() {
        return this.relationshipType;
    }
    
    public void setRelationshipType(final Integer relationshipType) {
        this.relationshipType = relationshipType;
    }
    
    public Integer getDimension() {
        return this.dimension;
    }
    
    public void setDimension(final Integer dimension) {
        this.dimension = dimension;
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

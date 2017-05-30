package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

@Deprecated
public class PositionRelationshipData extends BaseValueObject
{
    private static final long serialVersionUID = -4609302079503988945L;
    private Integer id;
    @NotNull
    private PositionIdentityBean sourceId;
    @NotNull
    private PositionIdentityBean destinationId;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public PositionRelationshipData() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PositionIdentityBean getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final PositionIdentityBean sourceId) {
        this.sourceId = sourceId;
    }
    
    public PositionIdentityBean getDestinationId() {
        return this.destinationId;
    }
    
    public void setDestinationId(final PositionIdentityBean destinationId) {
        this.destinationId = destinationId;
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

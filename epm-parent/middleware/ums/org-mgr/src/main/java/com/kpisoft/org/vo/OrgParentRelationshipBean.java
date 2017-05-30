package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class OrgParentRelationshipBean extends BaseValueObject
{
    private static final long serialVersionUID = -9043921399612470402L;
    private Integer id;
    @NotNull
    private OrgIdentityBean sourceIdentity;
    @NotNull
    private OrgIdentityBean destinationIdentity;
    @NotNull
    private Integer dimensionId;
    @NotNull
    private Date startDate;
    private Date endDate;
    
    public OrgParentRelationshipBean() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public OrgIdentityBean getSourceIdentity() {
        return this.sourceIdentity;
    }
    
    public void setSourceIdentity(final OrgIdentityBean sourceIdentity) {
        this.sourceIdentity = sourceIdentity;
    }
    
    public OrgIdentityBean getDestinationIdentity() {
        return this.destinationIdentity;
    }
    
    public void setDestinationIdentity(final OrgIdentityBean destinationIdentity) {
        this.destinationIdentity = destinationIdentity;
    }
    
    public Integer getDimensionId() {
        return this.dimensionId;
    }
    
    public void setDimensionId(final Integer dimensionId) {
        this.dimensionId = dimensionId;
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

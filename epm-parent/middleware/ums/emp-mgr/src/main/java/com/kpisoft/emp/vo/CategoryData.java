package com.kpisoft.emp.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class CategoryData extends BaseValueObject
{
    private static final long serialVersionUID = -6363514295496425619L;
    private Integer id;
    private String type;
    private Date startDate;
    private Date endDate;
    private Integer sourceId;
    private Integer level;
    private Integer entityId;
    private Integer status;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
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
    
    public Integer getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final Integer sourceId) {
        this.sourceId = sourceId;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
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
}

package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class OperationHierarchyData extends BaseValueObject
{
    private static final long serialVersionUID = 4461636572073348180L;
    private Integer id;
    @NotNull
    private Integer parentOperId;
    @NotNull
    private Integer childOperId;
    private Integer version;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getParentOperId() {
        return this.parentOperId;
    }
    
    public void setParentOperId(final Integer parentOperId) {
        this.parentOperId = parentOperId;
    }
    
    public Integer getChildOperId() {
        return this.childOperId;
    }
    
    public void setChildOperId(final Integer childOperId) {
        this.childOperId = childOperId;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
}
